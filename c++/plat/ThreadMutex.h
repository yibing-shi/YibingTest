// ThreadMutex.h: int32erface for the ThreadMutex class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_THREADMUTEX_H__C0CDFA2F_EE56_4319_81C2_255953443662__INCLUDED_)
#define AFX_THREADMUTEX_H__C0CDFA2F_EE56_4319_81C2_255953443662__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifdef WIN32
#include <windows.h>
#else
#include <pthread.h>
#endif
#include "config.h"

namespace PLAT_7G{

class ThreadMutex  
{
public:
	ThreadMutex();
	virtual ~ThreadMutex();

public:
    void lock();
    void unlock();

public:
#ifdef WIN32
    typedef HANDLE Handle_Type;
#else
    typedef pthread_mutex_t Handle_Type;
#endif
    Handle_Type& get_handle();

private:
    Handle_Type handle_;
};

} //end of namespace

#endif // !defined(AFX_THREADMUTEX_H__C0CDFA2F_EE56_4319_81C2_255953443662__INCLUDED_)

