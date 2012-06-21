// SockAddr.cpp: implementation of the SockAddr class.
//
//////////////////////////////////////////////////////////////////////

#ifdef WIN32
#include <windows.h>
#else
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <stdio.h>
#endif
#include <string>
#include "config.h"
#include "SockAddr.h"
#include "Exception.h"

namespace PLAT_7G{

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

SockAddr::SockAddr()
:port_(0), addr_(INADDR_ANY)
{
}

SockAddr::SockAddr(UINT16 port)
:port_(port), addr_(INADDR_ANY)
{
}

SockAddr::SockAddr(UINT16 port, const char* addr)
{
    set(port, addr);
}

SockAddr::SockAddr(UINT16 port, const std::string &addr)
{
    set(port, addr.c_str());
}

SockAddr::SockAddr(UINT16 port, UINT32 addr)
:port_(port), addr_(addr)
{
}

SockAddr::~SockAddr()
{

}

SockAddr::SockAddr(const SockAddr &rhs)
    :port_(rhs.port_), addr_(rhs.addr_)
{
}

SockAddr& SockAddr::operator=(const SockAddr &rhs)
{
    if (this == &rhs)
        return *this;
    port_ = rhs.port_;
    addr_ = rhs.addr_;
    return *this;
}

    
std::string SockAddr::getAddrString() const
{
    UINT32 networkAddr = htonl(addr_);
    BYTE *p = (BYTE*)&networkAddr;
    char buf[32];
    snprintf(buf, sizeof(buf), "%u.%u.%u.%u:%u", p[0], p[1], p[2], p[3], port_);
    return std::string(buf);
}

void SockAddr::set(UINT16 port, const char* addr)
{
    port_ = port;
    std::string strAddr(addr);
    std::string::size_type p = 0, q;

    addr_ = 0;
    INT32 i = 0;
    while ((q = strAddr.find('.', p)) != std::string::npos)
    {
        std::string strTemp = strAddr.substr(p, q);
        addr_ = (addr_ << 8) + atoi(strTemp.c_str());
        ++i;
        p = q + 1;
    }

    if (i < 3) 
    {
        addr_ = INADDR_ANY;
    }

    std::string strTemp = strAddr.substr(p);
    addr_ = (addr_ << 8) + atoi(strTemp.c_str());
}

void SockAddr::set(UINT16 port, UINT32 addr)
{
    port_ = port;
    addr_ = addr;
}

} //end of namespace

