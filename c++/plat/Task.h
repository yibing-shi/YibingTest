// Task.h: int32erface for the Task class.
//
//////////////////////////////////////////////////////////////////////

#ifndef __PLAT_TASK_HEADER__
#define __PLAT_TASK_HEADER__

#include "config.h"
#include "Thread.h"
#include "Queue.h"
#include "ThreadMutex.h"
#include "Condition.h"
#include "Message.h"

namespace PLAT_7G{

class Task : public Thread  
{
public:
	Task();
	virtual ~Task();

public:
    void putMessage(Message *plat_msg)
    {
        que_.putTail(plat_msg);
    }

    Message* getMessage()
    {
        return que_.getHead();
    }

    virtual INT32 svc();
    virtual INT32 onMessage(Message *msg) = 0;

    //Cancle the execution of this thread, usually called by other thread
    virtual void cancle();

public:
    UINT32 getCurConn()
    {
        return curPlatMsg->getConnID();
    }

private:
    typedef Condition<ThreadMutex> ThreadCondition;
    typedef Queue<Message*, ThreadMutex, ThreadCondition> _MsgQueue;
    _MsgQueue que_;
    
    Message *curPlatMsg;

};

} //end of namespace

#endif

