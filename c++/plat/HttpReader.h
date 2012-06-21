#ifndef __HTTP_READER_HEADER__
#define __HTTP_READER_HEADER__

namespace PLAT_7G{

class Sock;
class Dispatcher;

class HttpReader
{
public:
    HttpReader();
    ~HttpReader();

public:
    INT32 read_data(UINT32 conn_id, Sock& sock, Dispatcher& dispather);

private:
    enum Process_Result
    {
        CONTINUE = 1,
        FINISH = 2,
        FAIL = 3
    };
    Process_Result process_data(UINT32 conn_id, Dispatcher& dispather);
    INT32 reset_buffer();

private:
    char *msg_buf;
    UINT32 buf_len;
    UINT32 offset;
    UINT32 search_pos;
    BYTE head_gotten;
    UINT32 head_len;
    UINT32 content_len;
};

} //end of namespace

#endif

