// ThreadMutex.cpp: implementation of the ThreadMutex class.
//
//////////////////////////////////////////////////////////////////////

#include "ThreadMutex.h"
#include "Exception.h"

namespace PLAT_7G{

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

#ifdef WIN32
ThreadMutex::ThreadMutex()
: handle_(NULL)
{
    handle_ = CreateMutex(NULL, FALSE, NULL);
    if (handle_ == NULL)
    {
        throw Exception("Create mutex error!");
    }
    
}

ThreadMutex::~ThreadMutex()
{
    if (handle_)
    {
        CloseHandle(handle_);
    }
}


void ThreadMutex::lock()
{
    WaitForSingleObject(handle_, INFINITE);
}

void ThreadMutex::unlock()
{
    ReleaseMutex(handle_);
}

#else //WIN32


ThreadMutex::ThreadMutex()
{
    if (pthread_mutex_init(&handle_, NULL) != 0)
    {
        throw Exception("Create mutex error!");
    }
}

ThreadMutex::~ThreadMutex()
{
    pthread_mutex_destroy(&handle_);
}


void ThreadMutex::lock()
{
    pthread_mutex_lock(&handle_);
}

void ThreadMutex::unlock()
{
    pthread_mutex_unlock(&handle_);
}

ThreadMutex::Handle_Type& ThreadMutex::get_handle()
{
    return handle_;
}


#endif //WIN32


} //end of namespace

