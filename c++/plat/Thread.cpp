// Thread.cpp: implementation of the Thread class.
//
//////////////////////////////////////////////////////////////////////

#include "Thread.h"
#include "Logger.h"

namespace PLAT_7G{

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Thread::Thread()
:tid_(0)
{

}

Thread::~Thread()
{

}

#ifdef WIN32
DWORD WINAPI ThreadProc(void *param)
#else
void * ThreadProc(void *param)
#endif
{
    Thread *t = (Thread*)param;
    t->svc();
    return 0;
}


INT32 Thread::start()
{
    if (tid_ != 0)
    {
        LOG_ERROR("double start a Thread: %d!!!", tid_);
        return -1;
    }

#ifdef WIN32
    if ((tid_ = CreateThread(NULL, 0, ThreadProc, this, 0, NULL)) == NULL)
    {
        return -1;
    }
#else
    pthread_attr_t attr;
    if (pthread_attr_init(&attr) != 0
        || pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED) !=0)
    {
        return -1;
    }
    if (pthread_create(&tid_, &attr, ThreadProc, this) != 0)
    {
        return -1;
    }
#endif

    return 0;
}


} //end of namespace

