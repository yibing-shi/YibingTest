// Receiver.h: int32erface for the Receiver class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_RECEIVER_H__988F761F_817F_4C2A_8325_9305347B5CB9__INCLUDED_)
#define AFX_RECEIVER_H__988F761F_817F_4C2A_8325_9305347B5CB9__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "config.h"
#include "Sock.h"
#include "Thread.h"
#include "Logger.h"
#include "Dispatcher.h"

namespace PLAT_7G{

template <class Reader>
class Receiver : public Thread
{
public:
	Receiver(const Sock &sock, UINT32 conn_id, Dispatcher &dis);
	virtual ~Receiver();

protected:
    virtual INT32 svc();

private:
    Sock sock_;
    UINT32 conn_id_;
    Reader reader;
    Dispatcher &dis_;
};

template <class Reader>
Receiver<Reader>::Receiver(const Sock &sock, UINT32 conn_id, Dispatcher &dis)
:sock_(sock), conn_id_(conn_id), dis_(dis)
{

}

template <class Reader>
Receiver<Reader>::~Receiver()
{

}

template <class Reader>
INT32 Receiver<Reader>::svc()
{
    LOG_DEBUG("Receiver started for %s", 
        sock_.get_rmt_addr().getAddrString().c_str());
    while (TRUE)
    {
        if (reader.read_data(conn_id_, sock_, dis_) != 0)
        {
            LOG_ERROR("Receiver: read data error! close the socket");
            sock_.close();
            dis_.reconnect(conn_id_);
            break;
        }
    }

    LOG_INFO("Receiver is about to quit!");
    return 0;
}


} //end of namespace

#endif // !defined(AFX_RECEIVER_H__988F761F_817F_4C2A_8325_9305347B5CB9__INCLUDED_)

