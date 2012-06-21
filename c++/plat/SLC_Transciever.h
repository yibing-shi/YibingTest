#ifndef __SHORT_LIFECYCLE_CONNECTION_TRANSCIEVER_HEADER__
#define __SHORT_LIFECYCLE_CONNECTION_TRANSCIEVER_HEADER__

#ifdef WIN32
#pragma warning(disable: 4786)
#include <windows.h>
#else
#include <errno.h>
#include <sys/select.h>
#include <sys/socket.h>
#endif

#include <map>
#include <string.h>

#include "config.h"
#include "Thread.h"
#include "Sock.h"
#include "Dispatcher.h"
#include "Logger.h"


namespace PLAT_7G{

class Dispatcher;

template<class Reader>
class SLC_Transciever : public Thread
{
public:
    SLC_Transciever(Dispatcher &dis)
        :dis_(dis)
    {
    }
    
    virtual ~SLC_Transciever()
    {
    }


public:
    INT32 open ();

    virtual INT32 svc();

    INT32 register_conn(const Sock &sock, UINT32 conn_id);
    
    INT32 send_msg(HANDLE socket, 
        unsigned char *msg, UINT32 msg_len);
    
private:
    enum Conn_Status
    {
        RECEIVING = 1,
        SENDING = 2
    };

    enum Send_Result
    {
        ABORT = 0, 
        CONTINUE = 1,
        COMPLETE = 2
    };
    
    struct Conn
    {
        UINT32 id;
        Sock sock;
        Conn_Status status; 
        Reader reader;
        BYTE *snd_buf;
        UINT32 snd_buf_len;
        UINT32 snd_offset;

        Conn()
            :id(0), sock(), status(RECEIVING), reader(), 
             snd_buf(NULL), snd_buf_len(0), snd_offset(0)
        {
        }

        ~Conn()
        {
            if (snd_buf != NULL)
                delete[] snd_buf;
        }
    };

    struct To_Send
    {
        HANDLE socket;
        unsigned char * data;
        UINT32 data_len;
    };

    typedef std::map<HANDLE, Conn*> Conn_Container;
    Conn_Container conns;

    HANDLE rcv_fds[2];  //pipe for receive client connection sockets
    HANDLE snd_fds[2];  //pipe for receive out-sending messages

private:
    INT32 on_new_conn();

    INT32 on_send_data();
    
    INT32 close_conn(HANDLE handle);
    
    INT32 close_conn(Conn &conn);
    
    INT32 close_conn(typename Conn_Container::iterator pos);
    
    Send_Result send_i(Conn &conn);
    
    INT32 async_send_msg(HANDLE socket, 
        unsigned char *msg, UINT32 msg_len);

private:
    Dispatcher &dis_;

};

template<class Reader>
INT32 SLC_Transciever<Reader>::open()
{
    if (pipe(rcv_fds) != 0 || ::set_nonblock(rcv_fds[0]) != 0)
    {
        return -1;
    }

    if (pipe(snd_fds) != 0 || ::set_nonblock(snd_fds[0]) != 0)
    {
        return -1;
    }

    return start();
}

template<class Reader>
INT32 SLC_Transciever<Reader>::register_conn(const Sock &sock, UINT32 conn_id)
{
    Conn *new_conn = new Conn();
    new_conn->sock = sock;
    new_conn->id = conn_id;
    
    if (new_conn->sock.set_nonblock() != 0)
    {
        LOG_ERROR("set noblocking handle error, handle = %d", 
            new_conn->sock.get_handle());
        return -1;
    }
            
    if (write(rcv_fds[1], &new_conn, sizeof(new_conn)) != sizeof(new_conn))
    {
        return -1;
    }
    else
    {
        LOG_DEBUG("try to register a new conn to SLC_Transciever!");
        return 0;
    }
}
    
template<class Reader>
INT32 SLC_Transciever<Reader>::send_msg(
    HANDLE handle, unsigned char *msg, UINT32 msg_len)
{
    int send_bytes = send(handle, msg, msg_len, 0);
    if (send_bytes < 0)
    {
        if (errno == EAGAIN || errno == EWOULDBLOCK)
        {
            return async_send_msg(handle, msg, msg_len);
        }
        else
        {
            return -1;
        }
    }
    else if (send_bytes == (int)msg_len)
    {
        close_conn(handle);
        return 0;
    }
    else
    {
        return async_send_msg(handle, msg + send_bytes, msg_len - send_bytes);
    }
}

template<class Reader>
INT32 SLC_Transciever<Reader>::async_send_msg(
    HANDLE socket, unsigned char *msg, UINT32 msg_len)
{
    To_Send *to_send = new To_Send();
    to_send->socket = socket;
    to_send->data = new unsigned char[msg_len];
    ::memcpy(to_send->data, msg, msg_len);
    to_send->data_len = msg_len;

    if (write(snd_fds[1], &to_send, sizeof(to_send)) != sizeof(to_send))
    {
        delete to_send->data;
        delete to_send;
        return -1;
    }
    else
    {
        return 0;
    }
}

template<class Reader>
INT32 SLC_Transciever<Reader>::svc()
{
    int running = 1;
    fd_set rset;
    fd_set wset;
    while (running)
    {
        FD_ZERO(&rset);
        FD_ZERO(&wset);
        
        FD_SET(rcv_fds[0], &rset);
        FD_SET(snd_fds[0], &rset);
        HANDLE max_fd = rcv_fds[0] > snd_fds[0] ? rcv_fds[0]: snd_fds[0];
        typename Conn_Container::iterator it = conns.begin();
        while(it != conns.end())
        {
            Conn *conn = it->second;
            if (conn == NULL)
            {
                conns.erase(it++);
                continue;
            }

            if (conn->status == RECEIVING)
            {
                FD_SET((conn->sock).get_handle(), &rset);
            }
            else if (conn->status == SENDING)
            {
                FD_SET((conn->sock).get_handle(), &wset);
            }
            else
            {
                //should never reach here
                ++it;
                continue;
            }

            ++it;
            if ((conn->sock).get_handle() > max_fd)
            {
                max_fd = (conn->sock).get_handle();
            }
        }

        LOG_DEBUG("About to select, max_fd = %d", max_fd);
        int sel_ret = select(max_fd + 1, &rset, &wset, NULL, NULL);
        LOG_DEBUG("Select return = %d", sel_ret);
        switch (sel_ret)
        {
        case -1:
            running = 0;
            break;
        case 0:
            break;
        default:
            if (FD_ISSET(rcv_fds[0], &rset))
            {
                //new connection
                on_new_conn();
            }

            if (FD_ISSET(snd_fds[0], &rset))
            {
                //there is data on a socket to be sent
                on_send_data();
            }

            it = conns.begin();
            while (it != conns.end())
            {                
                Conn *conn = it->second;
                if(conn == NULL)
                {
                    conns.erase(it++);
                    continue;
                }

                HANDLE handle = (conn->sock).get_handle();
                if (conn->status == RECEIVING && FD_ISSET(handle, &rset)) 
                {                    
                    if ((conn->reader).read_data(conn->id, conn->sock, dis_) != 0)
                    {
                        LOG_ERROR("handle input error, close connection. socket = %d", handle);
                        close_conn(it);
                    }
                }
                
                if (conn->status == SENDING && FD_ISSET(handle, &wset))
                {
                    if (send_i(*conn) != CONTINUE)
                    {
                        LOG_ERROR("send data error, close connection. socket = %d", handle);
                        close_conn(it);
                    }
                }

                ++it;
            } //end of while
        } // end of switch
    }

    return -1;
}

template<class Reader>
INT32 SLC_Transciever<Reader>::on_new_conn()
{
    Conn *new_conn;
    while (1)
    {
        int bytes_read = read(rcv_fds[0], &new_conn, sizeof(new_conn));
        if (bytes_read < 0)
        {
            if (errno == EAGAIN || errno == EWOULDBLOCK)
            {
                LOG_DEBUG("no new socket to read");
                break;
            }
            else
            {
                LOG_ERROR("Reading new socket error!!!");
                return -1;
            }
        }
        else if (bytes_read != sizeof(new_conn))
        {
            LOG_DEBUG("read new connection data uncomplete!!!");
            break;
        }
        else
        {
            if (new_conn == NULL)
            {
                LOG_ERROR("the handler used to register is NULL!!!");
                return -1;
            }

            Sock &sock = new_conn->sock;
            new_conn->status = RECEIVING;
            conns[sock.get_handle()] = new_conn;
            LOG_DEBUG("find a new conn! handle = %d", sock.get_handle());
        }
    }

    return 0;
}

template<class Reader>
INT32 SLC_Transciever<Reader>::on_send_data()
{
    To_Send *to_send;
    while (read(rcv_fds[0], &to_send, sizeof(to_send)) == sizeof(to_send))
    {
        if (to_send == NULL)
        {
            LOG_ERROR("SLC_Transciever<Reader>::on_send_data, find NULL to send");
            continue;
        }
        typename Conn_Container::iterator it = conns.find(to_send->socket);
        if (it != conns.end())
        {
            Conn *conn = it->second;
            conn->status = SENDING;
            conn->snd_buf = to_send->data;
            conn->snd_buf_len = to_send->data_len;
            conn->snd_offset = 0;
            delete to_send;
        }
        else
        {
            delete to_send->data;
            delete to_send;
        }
    }

    return 0;
}

template<class Reader>
typename SLC_Transciever<Reader>::Send_Result 
SLC_Transciever<Reader>::send_i(Conn &conn)
{
    HANDLE hande = conn.sock.get_handle();
    int send_bytes = send(hande, conn.snd_buf + conn.snd_offset, 
        conn.snd_buf_len - conn.snd_offset, 0);
    if (send_bytes == -1)
	{
		if (errno != EAGAIN && errno != EWOULDBLOCK)
		{
            return ABORT;
		}
        else
        {
            return CONTINUE;
        }
	}
	else if (send_bytes == 0)
    {
        //do nothing
		return CONTINUE;
    }		
	else 
	{
        conn.snd_offset += send_bytes;
        if (conn.snd_offset < conn.snd_buf_len)
		    return CONTINUE;
        else
            return COMPLETE;  //将删除此连接
	}
}

template<class Reader>
INT32 SLC_Transciever<Reader>::close_conn(HANDLE handle)
{
    return close_conn(conns.find(handle));
}

template<class Reader>
INT32 SLC_Transciever<Reader>::close_conn(Conn &conn)
{
    return close_conn(conns.find(conn.sock.get_handle()));
}

template<class Reader>
INT32 SLC_Transciever<Reader>::close_conn(
    typename Conn_Container::iterator pos)
{
    if (pos == conns.end())
        return -1;

    Conn *conn = pos->second;
    conn->sock.close();
    //这里不能直接erase(it)，因为可能导致其他的iterator失效
    //统一在svc中处理
    delete (pos->second);
    pos->second = NULL;
    return 0;
}

} //end of namespace



#endif //__SHORT_LIFECYCLE_CONNECTION_TRANSCIEVER_HEADER__

