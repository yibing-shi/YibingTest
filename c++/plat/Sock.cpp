// Sock.cpp: implementation of the Sock class.
//
//////////////////////////////////////////////////////////////////////

#include "Sock.h"
#include "Exception.h"
#include <string.h>
#ifdef WIN32
#include <WINSOCK2.H>
#else
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <errno.h>
#include <unistd.h>
#include <fcntl.h>
#endif

namespace PLAT_7G{

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Sock::Sock()
    :localAddr_(), rmtAddr_()
{
    handle_ = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (handle_ == INVALID_SOCKET)
    {
        throw Exception("new Sock error!");
    }
}

Sock::Sock(sock_type handle)
    :handle_(handle), localAddr_(), rmtAddr_()
{
}

Sock::Sock(const Sock &rhs)
    :handle_(rhs.handle_), localAddr_(), rmtAddr_(rhs.rmtAddr_)
{
}

Sock::~Sock()
{
}

Sock& Sock::operator=(const Sock &rhs)
{
    if (this == &rhs)
        return *this;
    handle_ = rhs.handle_;
    localAddr_ = rhs.localAddr_;
    rmtAddr_ = rhs.rmtAddr_;
    return *this;
}

INT32 Sock::bind(const SockAddr &addr)
{
    struct sockaddr_in inetAddr;
    memset(&inetAddr, 0, sizeof(inetAddr));
    inetAddr.sin_family = AF_INET;
    inetAddr.sin_port = htons(addr.port_);
#ifdef WIN32
    inetAddr.sin_addr.S_un.S_addr = htonl(addr.addr_);
#else
    inetAddr.sin_addr.s_addr = htonl(addr.addr_);
#endif

    localAddr_ = addr;
    return ::bind(handle_, (const struct sockaddr*)&inetAddr, sizeof(inetAddr));
}

INT32 Sock::listen(INT32 backlog)
{
    return ::listen(handle_, backlog);
}


INT32 Sock::accept(Sock &cltSock)
{
    sock_type cltHandle;
    sockaddr_in inetAddr;
    sock_len_type addrLen = sizeof(inetAddr);
    if ((cltHandle = ::accept(handle_, (sockaddr*)&inetAddr, &addrLen)) == INVALID_SOCKET)
    {
        return -1;
    }

    cltSock.handle_ = cltHandle;
    cltSock.localAddr_ = localAddr_;
#ifdef WIN32
    cltSock.rmtAddr_.set(ntohs(inetAddr.sin_port), 
        ntohl(inetAddr.sin_addr.S_un.S_addr));
#else
    cltSock.rmtAddr_.set(ntohs(inetAddr.sin_port), 
        ntohl(inetAddr.sin_addr.s_addr));
#endif

    return 0;
}

INT32 Sock::connect(const SockAddr &rmtAddr)
{
    struct sockaddr_in inetAddr;
    memset(&inetAddr, 0, sizeof(inetAddr));
    inetAddr.sin_family = AF_INET;
    inetAddr.sin_port = htons(rmtAddr.port_);
#ifdef WIN32
    inetAddr.sin_addr.S_un.S_addr = htonl(rmtAddr.addr_);
#else
    inetAddr.sin_addr.s_addr = htonl(rmtAddr.addr_);
#endif

    rmtAddr_ = rmtAddr;
    return ::connect(handle_, (const struct sockaddr*)&inetAddr, sizeof(inetAddr));
}

INT32 Sock::recv_n(BYTE *buffer, UINT32 number)
{
    UINT32 totalRead = 0;
    UINT32 bytesRead;
    while(totalRead < number)
    {
        bytesRead = ::recv(handle_, (char*)buffer + totalRead, number - totalRead, 0);
        
        if (bytesRead == SOCKET_ERROR && get_os_err() != EWOULDBLOCK && get_os_err()!= EAGAIN)
        {
            //some error occured!
            return -1;
        }
        
        if (bytesRead == 0)
        {
            //socket is closed gracefully
            return -1;
        }
            
        totalRead += bytesRead;
    }
    return 0;
}

INT32 Sock::recv(BYTE *buffer, UINT32 buf_len)
{
    return ::recv(handle_, (char*)buffer, buf_len, 0);
}

INT32 Sock::send_n(BYTE *buffer, UINT32 size)
{
    UINT32 totalSent = 0;
    UINT32 bytesSent;

    while (totalSent < size) {
        bytesSent = ::send(handle_, (char*)buffer + totalSent, size - totalSent, 0);
        
        if (bytesSent == SOCKET_ERROR && get_os_err() != EWOULDBLOCK && get_os_err()!= EAGAIN)
        {
            //some error occured!
            return -1;
        }
        
        if (bytesSent == 0)
        {
            //socket is closed gracefully
            return -1;
        }
            
        totalSent += bytesSent;
    }

    return 0;
}

INT32 Sock::set_nonblock()
{
#ifdef WIN32
    return 0;
#else
    return ::set_nonblock(handle_);
#endif
}

INT32 Sock::set_reuse()
{
    int reuse = 1;
    return ::setsockopt(handle_, SOL_SOCKET, SO_REUSEADDR, 
        &reuse, sizeof(reuse));
}

} //end of namespace

