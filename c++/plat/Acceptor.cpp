// Acceptor.cpp: implementation of the Acceptor class.
//
//////////////////////////////////////////////////////////////////////
#include "Acceptor.h"
#include "Dispatcher.h"
#include "Logger.h"

namespace PLAT_7G{

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

const UINT16 Acceptor::LISTEN_LEN = 10;

Acceptor::Acceptor(UINT16 port, Dispatcher &dis)
:sock_(), addr_(port), dis_(dis)
{

}

Acceptor::Acceptor(const char *addr, UINT16 port, Dispatcher &dis)
:sock_(), addr_(port, addr), dis_(dis)
{

}

Acceptor::~Acceptor()
{

}

INT32 Acceptor::svc()
{
    if (sock_.set_reuse() != 0)
    {
        LOG_ERROR("set reuse address flag error");
        return -1;
    }
    
    if (sock_.bind(addr_) != 0)
    {
        LOG_ERROR("Socket bind error! error = %d", get_os_err());
        return -1;
    }
    
    if (sock_.listen(LISTEN_LEN) != 0)
    {
        LOG_ERROR("Socket listen error! error = %d", get_os_err());
        return -1;
    }

    while (1)
    {
        Sock cltSock;
        if (sock_.accept(cltSock) != 0)
            break;
        
        LOG_DEBUG("Accept a new connection! client = %s", 
            cltSock.get_rmt_addr().getAddrString().c_str());

        dis_.register_in_conn(cltSock);
    }
    return 0;
}

} //end of namespace

