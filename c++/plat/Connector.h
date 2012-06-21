// Connector.h: interface for the Connector class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_CONNECTOR_H__E454365E_0863_40C4_82F2_D72B1AE1D819__INCLUDED_)
#define AFX_CONNECTOR_H__E454365E_0863_40C4_82F2_D72B1AE1D819__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "config.h"
#include "Sock.h"
#include "SockAddr.h"
#include "Thread.h"

namespace PLAT_7G
{

class Dispatcher;

class Connector : public Thread  
{
public:
	Connector(UINT16 rmtPort, const char* rmtAddr, Dispatcher &dis);
    Connector(const SockAddr &addr, Dispatcher &dis);
	virtual ~Connector();

public:
    virtual INT32 svc();

private:
    Sock sock_;
    SockAddr rmt_addr_;

    Dispatcher &dis_;
};


} // end of namespace

#endif // !defined(AFX_CONNECTOR_H__E454365E_0863_40C4_82F2_D72B1AE1D819__INCLUDED_)

