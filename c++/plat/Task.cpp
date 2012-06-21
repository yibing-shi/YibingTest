// Task.cpp: implementation of the Task class.
//
//////////////////////////////////////////////////////////////////////

#include <memory>
#include "Task.h"
#include "Logger.h"

namespace PLAT_7G{

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Task::Task()
:que_()
{

}

Task::~Task()
{

}

INT32 Task::svc()
{
    while (TRUE)
    {
        std::auto_ptr<Message> msg(getMessage());
        curPlatMsg = msg.get();

        if (curPlatMsg->getType() == Message::QUIT_MESSAGE)
        {
            LOG_INFO("Task: receive a quit message, quit the task");
            break;
        }
        
        if (onMessage(curPlatMsg) != 0)
        {
            LOG_ERROR("Task: process message fail, quit");
            break;
        }
        //delete msg;
    }

    LOG_INFO("Task is about to quit!");
    return 0;
}

void Task::cancle()
{
    Message *msg = new Message();
    msg->setType(Message::QUIT_MESSAGE);
    this->putMessage(msg);
}


} //end of namespace

