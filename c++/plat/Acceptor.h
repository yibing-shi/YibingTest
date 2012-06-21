// Acceptor.h: int32erface for the Acceptor class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_ACCEPTOR_H__B4D0436D_E3A6_47FD_8FFE_524142E2924C__INCLUDED_)
#define AFX_ACCEPTOR_H__B4D0436D_E3A6_47FD_8FFE_524142E2924C__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "config.h"
#include "Sock.h"
#include "SockAddr.h"
#include "Thread.h"

namespace PLAT_7G{

class Dispatcher;

class Acceptor  : public Thread
{
public:
	Acceptor(UINT16 port, Dispatcher &dis);
	Acceptor(const char* addr, UINT16 port, Dispatcher &dis);
	virtual ~Acceptor();

protected:
    virtual INT32 svc();

private:
    static const UINT16 LISTEN_LEN;
private:
    Sock sock_;
    SockAddr addr_;
    Dispatcher &dis_;
};

} //end of namespace

#endif // !defined(AFX_ACCEPTOR_H__B4D0436D_E3A6_47FD_8FFE_524142E2924C__INCLUDED_)

