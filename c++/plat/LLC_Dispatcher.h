#ifndef __LONG_LIFECYCLE_CONNECTION_DISPATCHER__
#define __LONG_LIFECYCLE_CONNECTION_DISPATCHER__

#include "Dispatcher.h"
#include "ThreadMutex.h"
#include "Sock.h"
#include "Receiver.h"
#include "Sender.h"
#include "Connector.h"
#include "DelayRemover.h"
#include <map>
#include <string>

namespace PLAT_7G{


template<class Reader>
class LLC_Dispatcher : public Dispatcher
{
public:
	LLC_Dispatcher(BYTE id) 
        : Dispatcher(id), conn_map(), map_lock(), conn_seq(0)
    {
    }
    
    virtual ~LLC_Dispatcher() 
    {
    }

    virtual UINT32 async_connect(const SockAddr& addr);

    virtual INT32 register_in_conn(const Sock &sock);

    virtual INT32 register_out_conn(const Sock &sock);

    virtual INT32 send_msg(UINT32 conn_id, 
        BYTE *data, UINT32 data_len);

    // Children can overide these to provide more sophysticated 
    // operations when connectin status changed
    virtual INT32 on_connected(UINT32 conn_id) {return 0;}
    virtual INT32 on_accepted(UINT32 conn_id) {return 0;}
    virtual INT32 on_sent(UINT32 conn_id) {return 0;}
    

public:
    typedef Receiver<Reader> Receiver_Type;

protected:
    enum Direction
    {
        UNKNOWN = 0,
        INBOUND = 1,
        OUTBOUND = 2
    };
    UINT16 conn_seq;
    UINT32 gen_conn_id(BYTE direction)
    {
        return ((((UINT32)get_id()) << 24) 
                | (((UINT32)direction) << 16)
                | (conn_seq++ & 0xFFFF));
    }
    BYTE get_direction(UINT32 conn_id)
    {
        return (BYTE)((conn_id >> 16) & 0x000000FF);
    }

    virtual INT32 reconnect(UINT32 conn_id);

    virtual INT32 close_conn(UINT32 conn_id);

private:
    static const UINT32 INVALID_CONN_ID = 0xFFFFFFFF;

    enum Conn_Status
    {
        CONNECTED = 0,
        DISCONNECTED = 1,
        CONNECTING = 2
    };
    
    struct Conn
    {
        UINT32 conn_id;
        Direction direction;
        Sock sock;
        SockAddr rmt_addr;
        Conn_Status status;
        Receiver_Type *receiver;
        Sender *sender;
        Connector *connector;

        Conn()
            :conn_id(INVALID_CONN_ID), direction(UNKNOWN), 
             sock(), rmt_addr(), receiver(NULL), 
             status(DISCONNECTED), sender(NULL), connector(NULL)
        {
        }

        Conn(UINT32 id, Direction direct, 
             const SockAddr &addr, Conn_Status st, 
             Receiver_Type *r, Sender *s, Connector *c)
            :conn_id(id), direction(direct), sock(), 
             rmt_addr(addr), status(st), receiver(r), 
             sender(s), connector(c)
        {
        }

        ~Conn() {}

        Conn(const Conn &rhs)
            :conn_id(rhs.conn_id), direction(rhs.direction), 
             status(rhs.status), rmt_addr(rhs.rmt_addr), 
             receiver(rhs.receiver), sender(rhs.sender), 
             connector(rhs.connector)
        {
        }

        Conn& operator=(const Conn& rhs)
        {
            if (&rhs == this)
                return *this;
            conn_id = rhs.conn_id;
            direction = rhs.direction;
            status = rhs.status;
            rmt_addr = rhs.rmt_addr;
            receiver = rhs.receiver;
            sender = rhs.sender;
            connector = rhs.connector;
            return *this;
        }
    };
    
    typedef std::map<UINT32, Conn> MapType;
    MapType conn_map;
    ThreadMutex map_lock;
};


template<class Reader>
UINT32 LLC_Dispatcher<Reader>::async_connect(const SockAddr &addr)
{
    Connector *connector = new Connector(addr, *this);
    UINT32 conn_id = gen_conn_id(OUTBOUND);
    Conn conn(conn_id, OUTBOUND, addr, 
        CONNECTING, NULL, NULL, connector);
    
    {
        GUARD(ThreadMutex, map_lock, g);
        conn_map[conn_id] = conn;
    }

    LOG_DEBUG("LLC_Dispatcher: Connecting to %s, conn_id = %d", 
        addr.getAddrString().c_str(), conn_id);
        
    connector->start();
    
    return conn_id;
}

template<class Reader>
INT32 LLC_Dispatcher<Reader>::register_in_conn(const Sock &sock)
{
    LOG_DEBUG("LLC_Dispatcher: a new inbound connection: %s",
        sock.get_rmt_addr().getAddrString().c_str());
    Conn conn;
    conn.conn_id = gen_conn_id(INBOUND);
    conn.direction = INBOUND;
    conn.rmt_addr = sock.get_rmt_addr();
    conn.sock = sock;
    conn.status = CONNECTED;
    conn.receiver = new Receiver_Type(sock, conn.conn_id, *this);
    conn.receiver->start();
    conn.sender = new Sender(sock);
    conn.sender->start();
    conn.connector = NULL;
    
    {
        GUARD(ThreadMutex, map_lock, g);
        conn_map[conn.conn_id] = conn;
    }

    on_accepted(conn.conn_id);
    
    return 0;
}

template<class Reader>
INT32 LLC_Dispatcher<Reader>::register_out_conn(const Sock &sock)
{
    LOG_DEBUG("LLC_Dispatcher: a new outbound connection: %s",
        sock.get_rmt_addr().getAddrString().c_str());
    GUARD(ThreadMutex, map_lock, g);
    const SockAddr &rmtAddr = sock.get_rmt_addr();
    typename MapType::iterator it = conn_map.begin();
    for (; it != conn_map.end(); ++it)
    {
        Conn &conn = it->second;
        if (conn.direction != OUTBOUND 
            || conn.status != CONNECTING
            || rmtAddr != conn.rmt_addr)
            continue;

        LOG_DEBUG("find the conn item (%d), about to start receiver & sender", 
            conn.conn_id);
        conn.sock = sock;
        conn.status = CONNECTED;
        conn.receiver = new Receiver_Type(sock, conn.conn_id, *this);
        conn.sender = new Sender(sock);
        conn.receiver->start();
        conn.sender->start();
        ThreadDelayRemover::instance()->add_item(conn.connector);
        conn.connector = NULL;
        on_connected(conn.conn_id);
        break;
    }

    if (it == conn_map.end())
    {
        LOG_ERROR("LLC_Dispatcher: update info for outbound conn fail!");
        Sock tmpSock = sock;
        tmpSock.close();
        return -1;
    }
    else
    {
        LOG_DEBUG("LLC_Dispatcher: update info for outbound conn succeed!");
        return 0;
    }
}

template<class Reader>
INT32 LLC_Dispatcher<Reader>::send_msg(UINT32 conn_id, 
    BYTE *data, UINT32 data_len)
{
    LOG_DEBUG("LLC_Dispatcher: Entering send_msg, conn_id = %d, "
        "data_len = %d", conn_id, data_len);
    GUARD(ThreadMutex, map_lock, g);
    typename MapType::iterator it = conn_map.find(conn_id);
    if (it == conn_map.end())
    {
        LOG_ERROR("LLC_Dispatcher: cannot find connection: %d", conn_id);
        return -1;
    }

    Conn &conn = it->second;
    Sender *sender = conn.sender;
    if (sender == NULL)
    {
        LOG_ERROR("LLC_Dispatcher: cannot find sender for conn: %d", conn_id);
        return -1;
    }

    sender->sendMsg(data, data_len);
    
    return 0;
}

template<class Reader>
INT32 LLC_Dispatcher<Reader>::reconnect(UINT32 conn_id)
{
    GUARD(ThreadMutex, map_lock, g);
    typename MapType::iterator it = conn_map.find(conn_id);
    if (it == conn_map.end())
    {
        LOG_ERROR("LLC_Dispatcher: cannot find the connection to "
            "reconnect, conn_id = %u", conn_id);
        return -1;
    }

    Conn &conn = it->second;
    if (get_direction(conn.conn_id) == INBOUND)
    {
        LOG_DEBUG("LLC_Dispatcher: connection %d donesn't need be "
            "reconnected. It is a inbound one", conn_id);
        return 0;
    }

    //Usually, the receiver has quit because of error, so just cancle 
    //the sender
    conn.sender->cancle();
    ThreadDelayRemover::instance()->add_item(conn.receiver);
    ThreadDelayRemover::instance()->add_item(conn.sender);
    
    conn.status = DISCONNECTED;
    conn.connector = new Connector(conn.rmt_addr, *this);
    conn.connector->start();
    
    return 0;
}

template<class Reader>
INT32 LLC_Dispatcher<Reader>::close_conn(UINT32 conn_id)
{
    GUARD(ThreadMutex, map_lock, g);
    typename MapType::iterator it = conn_map.find(conn_id);
    if (it == conn_map.end())
    {
        LOG_ERROR("LLC_Dispatcher: cannot find the connection to "
            "close, conn_id = %u", conn_id);
        return -1;
    }

    Conn &conn = it->second;
    if (conn.status == CONNECTED)
    {
        conn.sock.close();
        //Usually, the receiver will quit when the sock is closed.
        conn.sender->cancle();
        ThreadDelayRemover::instance()->add_item(conn.receiver);
        ThreadDelayRemover::instance()->add_item(conn.sender);
    }
    
    conn_map.erase(it);
    
    return 0;
}

} //end of namespace


#endif

