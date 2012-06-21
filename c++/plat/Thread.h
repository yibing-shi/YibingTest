// Thread.h: int32erface for the Thread class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_THREAD_H__3408CD7A_99BF_41A4_A435_57AE16F7B952__INCLUDED_)
#define AFX_THREAD_H__3408CD7A_99BF_41A4_A435_57AE16F7B952__INCLUDED_

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

class Thread  
{
#ifdef WIN32
    friend DWORD WINAPI ThreadProc(void *param);
#else
    friend void * ThreadProc(void *param);
#endif
public:
    Thread();
    virtual ~Thread();

public:
#ifdef WIN32
    typedef HANDLE thread_t;
#else
    typedef pthread_t thread_t;
#endif

public:
    INT32 start(); //return 0 means start the thread successfully

protected:
    virtual INT32 svc() = 0;

private:
    thread_t tid_;
};

} //end of namespace

#endif // !defined(AFX_THREAD_H__3408CD7A_99BF_41A4_A435_57AE16F7B952__INCLUDED_)

