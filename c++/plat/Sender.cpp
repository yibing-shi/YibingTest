// Sender.cpp: implementation of the Sender class.
//
//////////////////////////////////////////////////////////////////////

#include "Sender.h"
#include "Logger.h"
#include <memory>
#include <string.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////


namespace PLAT_7G{

Sender::Sender(const Sock &socket)
:sock_(socket)
{

}

Sender::~Sender()
{

}

void Sender::sendMsg(BYTE *msg, UINT32 length)
{
    Message *to_send = new Message(msg, length);
    this->putMessage(to_send);
}

INT32 Sender::onMessage(Message *msg)
{
    if (sock_.send_n(msg->getBody(), msg->getLength()) != 0)
    {
        LOG_ERROR("Send message error! will quit");
        return -1;
    }

    return 0;
}

} //end of namespace

