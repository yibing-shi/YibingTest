#include "config.h"
#include "HttpReader.h"
#include "Sock.h"
#include "Dispatcher.h"
#include "Logger.h"
#include <string.h>
#include <cstdio>


namespace PLAT_7G{


#define DFT_HEAD_BUF_LEN 10240

HttpReader::HttpReader()
    :msg_buf(NULL), buf_len(0), offset(0), search_pos(0),
     head_gotten(0), head_len(0), content_len(0)
{
    msg_buf = new char[DFT_HEAD_BUF_LEN];
    buf_len = DFT_HEAD_BUF_LEN;
}

HttpReader::~HttpReader()
{
    delete[] msg_buf;
}

INT32 HttpReader::read_data(UINT32 conn_id, Sock& sock, 
    Dispatcher& dispather)
{
    LOG_DEBUG("Entering HttpReader::read_data");
    
    INT32 recv_bytes = sock.recv((BYTE*)msg_buf + offset, 
        buf_len - offset);
    if (recv_bytes <= 0)
    {
        LOG_ERROR("HttpReader: read data error, conn = %d, error = %d", 
            sock.get_handle(), get_os_err());
        return -1;
    }
    LOG_DEBUG("HttpReader: read %u bytes", recv_bytes);
    offset += recv_bytes;
    msg_buf[offset] = '\0';

    while (1)
    {
        switch (process_data(conn_id, dispather))
        {
        case FINISH:
            return 0;
        case FAIL:
            return -1;
        default:
            //do nothing, go on processing the remaining data
            ;
        }
    }
}

HttpReader::Process_Result 
HttpReader::process_data(UINT32 conn_id, Dispatcher& dispather)
{
    if (head_gotten == 0)
    {
        //尚未读完头部
        if (offset >= 4)
        {
            for (UINT32 i = search_pos; i <= offset - 4; ++i)
            {
                if (msg_buf[i] == '\r' 
                    && msg_buf[i + 1] == '\n'
                    && msg_buf[i + 2] == '\r'
                    && msg_buf[i + 3] == '\n')
                {
                    //读到了头部
                    head_gotten = 1;
                    head_len = i + 4;
                    break;
                }
            }
            if (head_gotten == 0)
                search_pos = offset - 4 + 1;
        }
    }

    if (head_gotten == 0)
    {
        //仍然没有读完头部，退出，下次再读
        LOG_DEBUG("HttpReader: the head hasn't been got, continue reading");
        return FINISH;
    }

    if (content_len == 0)
    {
        const char *con_len_pos;
        con_len_pos = ::strstr (msg_buf, "\nContent-length:");
        if (con_len_pos == 0)
            con_len_pos = ::strstr (msg_buf, "\nContent-Length:");
        if (con_len_pos == 0)
        {
            //This is a HTTP Package containing no body    //生成请求消息,并通过dispatcher转发给业务线程处理
            LOG_DEBUG("HttpReader: Get a new message and send to dispatcher.");
            dispather.on_new_msg(conn_id, (BYTE*)msg_buf, head_len);

            if (reset_buffer() != 0)
            {
                LOG_ERROR("reset buffer error");
                return FAIL;
            }
            
            return CONTINUE;
        }

        //try to read the body length
        if (::sscanf (con_len_pos, "\nContent-%*[lL]ength: %d", &content_len) != 1 
            || content_len > DFT_HEAD_BUF_LEN - 1)
        {
            LOG_ERROR("HttpReader: Read content length failed");
            return FAIL;
        }
    }

    if (buf_len < head_len + content_len)
    {
        //缓冲区长度不够，生成新的缓冲区
        char *new_buf = new char[head_len + content_len];
        if (new_buf == NULL)
        {
            LOG_ERROR("allocate new buffer error: %d", get_os_err());
            return FAIL;
        }
        ::memcpy(new_buf, msg_buf, offset);
        delete msg_buf;
        msg_buf = new_buf;
        buf_len = head_len + content_len;
        //此时包肯定尚未读完，下次继续读
        LOG_DEBUG("HttpReader: The whole package hasn't been got, "
            "continue reading.");
        return FINISH;
    }

    if (offset < head_len + content_len)
    {
        //包尚未读完，下次继续读
        LOG_DEBUG("HttpReader: The whole package hasn't been got, "
            "continue reading.");
        return FINISH;
    }

    //生成请求消息,并通过dispatcher转发给业务线程处理
    LOG_DEBUG("HttpReader: Get a new message and send to dispatcher.");
    dispather.on_new_msg(conn_id, (BYTE*)msg_buf, head_len + content_len);

    //重新设置缓冲区
    if (reset_buffer() != 0)
    {
        LOG_ERROR("reset buffer error");
        return FAIL;
    }

    return CONTINUE;
}

INT32 HttpReader::reset_buffer()
{    
    char *remaining = NULL;
    UINT32 remain_len = 0;
    if (offset > head_len + content_len)
    {
        remaining = msg_buf + head_len + content_len;
        remain_len = offset - (head_len + content_len);
    }
    
    msg_buf = new char[DFT_HEAD_BUF_LEN];
    if (msg_buf == NULL)
    {
        return -1;
    }
    
    buf_len = DFT_HEAD_BUF_LEN;
    offset = 0;
    search_pos = 0;
    head_gotten = 0;
    head_len = 0;
    content_len = 0;

    if (remaining != NULL)
    {
        ::memcpy(msg_buf, remaining, remain_len);
        offset = remain_len;
    }
    
    return 0;
}


} //end of namespace

