// Condition.h: int32erface for the Condition class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_EVENTCONDITION_H__DC48674F_C4F4_4977_94BE_72D2544D9E14__INCLUDED_)
#define AFX_EVENTCONDITION_H__DC48674F_C4F4_4977_94BE_72D2544D9E14__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifdef WIN32
#include <windows.h>
typedef HANDLE Cond_Type;
#else
#include <pthread.h>
typedef pthread_cond_t Cond_Type;
#endif
#include "config.h"
#include "Exception.h"

namespace PLAT_7G{

template<class LOCK>
class Condition  
{
public:
	Condition();
	virtual ~Condition();

public:
    void wait(LOCK &lock);
    void signal();

private:
    Cond_Type handle_;
};

template<class LOCK>
Condition<LOCK>::Condition()
{
#ifdef WIN32
    handle_ = CreateEvent(NULL, FALSE, FALSE, NULL);
    if (handle_ == NULL)
    {
        throw Exception("Create Condition error!");
    }
#else
    if (pthread_cond_init(&handle_, NULL) != 0)
    {
        throw Exception("Create Condition error!");
    }
#endif
}

template<class LOCK>
Condition<LOCK>::~Condition()
{
#ifndef WIN32
    pthread_cond_destroy(&handle_);
#endif    
}

template<class LOCK>
void Condition<LOCK>::wait(LOCK &lock)
{
#ifdef WIN32
    lock.unlock();
    WaitForSingleObject(handle_, INFINITE);
    lock.lock();
#else
    pthread_cond_wait(&handle_, &lock.get_handle());
#endif
}

template<class LOCK>
void Condition<LOCK>::signal()
{
#ifdef WIN32
    SetEvent(handle_);
#else
    pthread_cond_signal(&handle_);
#endif
}


} //end of namespace

#endif // !defined(AFX_EVENTCONDITION_H__DC48674F_C4F4_4977_94BE_72D2544D9E14__INCLUDED_)

