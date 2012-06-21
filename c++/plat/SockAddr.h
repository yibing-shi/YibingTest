// SockAddr.h: int32erface for the SockAddr class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SOCKADDR_H__3DA44667_7A6F_405D_B2F1_B3DB9C295664__INCLUDED_)
#define AFX_SOCKADDR_H__3DA44667_7A6F_405D_B2F1_B3DB9C295664__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <string>
#include "config.h"

namespace PLAT_7G{

class SockAddr  
{
    friend class Sock;
    friend bool operator==(const SockAddr& lhs, const SockAddr &rhs);
    friend bool operator!=(const SockAddr& lhs, const SockAddr &rhs);
public:
    /*所有整数类型，使用主机序即可*/
    SockAddr();
    SockAddr(UINT16 port);
	SockAddr(UINT16 port, const char* addr);
    SockAddr(UINT16 port, const std::string& addr);
	SockAddr(UINT16 port, UINT32 addr);
	virtual ~SockAddr();

    SockAddr(const SockAddr &rhs);
    SockAddr& operator=(const SockAddr &rhs);

	void set(UINT16 port, const char* addr);
	void set(UINT16 port, UINT32 addr);
public:
    std::string getAddrString() const;

private:
    UINT16 port_;
    UINT32 addr_;
};

inline bool operator==(const SockAddr& lhs, const SockAddr &rhs)
{
    return lhs.port_ == rhs.port_ && lhs.addr_ == rhs.addr_;
}

inline bool operator!=(const SockAddr& lhs, const SockAddr &rhs)
{
    return lhs.port_ != rhs.port_ || lhs.addr_ != rhs.addr_;
}

} //end of namespace

#endif // !defined(AFX_SOCKADDR_H__3DA44667_7A6F_405D_B2F1_B3DB9C295664__INCLUDED_)

