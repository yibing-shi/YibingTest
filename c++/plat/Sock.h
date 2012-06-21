// Sock.h: int32erface for the Sock class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SOCK_H__7951A4D8_8AC0_4122_ADAD_9928B3ABADCE__INCLUDED_)
#define AFX_SOCK_H__7951A4D8_8AC0_4122_ADAD_9928B3ABADCE__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifdef WIN32
#include <windows.h>
#endif
#include "config.h"
#include "SockAddr.h"

namespace PLAT_7G{

class Sock  
{
public:
#ifdef WIN32
    typedef SOCKET sock_type;
#else
    typedef HANDLE sock_type;
#endif

public:
	Sock();
    Sock(sock_type handle);
	virtual ~Sock();

    Sock(const Sock &rhs);
    Sock& operator=(const Sock &rhs);

    sock_type get_handle()
    {
        return handle_;
    }
    
    sock_type get_handle() const
    {
        return handle_;
    }


public:
	INT32 set_nonblock();
    INT32 set_reuse();
	INT32 accept(Sock& cltSock);
	INT32 listen(INT32 backlog);
	INT32 bind(const SockAddr &addr);
    INT32 connect(const SockAddr &rmtAddr);
	INT32 send_n(BYTE* buffer, UINT32 size);
	INT32 recv_n(BYTE *buffer, UINT32 number);
    INT32 recv(BYTE *buffer, UINT32 buf_len);

    void close()
    {
#ifdef WIN32
        ::shutdown(handle_, SD_BOTH);
#else
        ::close(handle_);
#endif
    }

    const SockAddr& get_rmt_addr() const
    {
        return rmtAddr_;
    }

    const SockAddr& get_local_addr() const
    {
        return localAddr_;
    }

private:
#ifdef WIN32
    typedef INT32 sock_len_type;
#else
    typedef UINT32 sock_len_type;
#endif

private:
    sock_type handle_;
    SockAddr localAddr_;
    SockAddr rmtAddr_;
};

} //end of namespace

#endif // !defined(AFX_SOCK_H__7951A4D8_8AC0_4122_ADAD_9928B3ABADCE__INCLUDED_)

