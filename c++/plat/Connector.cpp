// Connector.cpp: implementation of the Connector class.
//
//////////////////////////////////////////////////////////////////////

#include "Connector.h"
#include "Logger.h"
#include "Dispatcher.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////
namespace PLAT_7G
{


Connector::Connector(UINT16 rmtPort, const char* rmtAddr, Dispatcher &dis)
:rmt_addr_(rmtPort, rmtAddr), sock_(), dis_(dis)
{

}

Connector::Connector(const SockAddr &addr, Dispatcher &dis)
:rmt_addr_(addr), sock_(), dis_(dis)
{

}

Connector::~Connector()
{
}

INT32 Connector::svc()
{
    for (int i = 0; sock_.connect(rmt_addr_) != 0; ++i)
    {
        LOG_WARN("Connect to %s fail for %d time", 
            rmt_addr_.getAddrString().c_str(), i + 1);
        ::sleep(1);
    }
        
    LOG_DEBUG("Connect to %s succeed!", rmt_addr_.getAddrString().c_str());
    
    dis_.register_out_conn(sock_);

    return 0;
}

} //end of namespace

