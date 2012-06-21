// Dispatcher.h: int32erface for the Dispatcher class.
//
//////////////////////////////////////////////////////////////////////

#ifndef __DISPATCHER_HEADER__
#define __DISPATCHER_HEADER__

#include <map>
#include "config.h"
#include "Message.h"
#include "Task.h"
#include "ThreadMutex.h"
#include "Singleton.h"

namespace PLAT_7G{

class Sock;

class Dispatcher 
{
public:
	Dispatcher(BYTE id) : id_(id) {}
	virtual ~Dispatcher() {}

    BYTE get_id()
    {
        return id_;
    }

    //called by Acceptor to register a new income connection
    virtual INT32 register_in_conn(const Sock &sock) = 0;

    //called by Connector to register a new outcome connection
    virtual INT32 register_out_conn(const Sock &sock) = 0;
    
    //called by Receiver or Transciever, transfering a new message 
    //to service tasks to process
    virtual INT32 on_new_msg(UINT32 conn_id, 
        BYTE *data, UINT32 data_len) = 0;

    //called by service tasks to send message out
    virtual INT32 send_msg(UINT32 conn_id, 
        BYTE *data, UINT32 data_len) = 0;

    //called by receiver to re-construct a connection
    virtual INT32 reconnect(UINT32 conn_id) {}

private:
    BYTE id_;
};



} //end of namespace

#endif 

