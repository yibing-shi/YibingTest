#include "config.h"
#ifdef WIN32
#include <windows.h>
#else
#include <errno.h>
#include <pthread.h>
#include <unistd.h>
#include <fcntl.h>
#endif

INT32 get_os_err()
{
#ifdef WIN32
    return WSAGetLastError();
#else
    return errno;
#endif
}

UINT32 get_process_id()
{
#ifdef WIN32
    return GetCurrentProcessId();
#else
    return getpid();
#endif
}

UINT32 get_thread_id()
{
#ifdef WIN32
    return GetCurrentThreadId();
#else
    return pthread_self();
#endif
}

INT32 set_nonblock(HANDLE handle)
{
#ifdef WIN32
    return 0;
#else
    int flag;
    if ((flag = fcntl(handle, F_GETFL, 0)) == -1
        || fcntl(handle, F_SETFL, flag | O_NONBLOCK) == -1)
    {
        return -1;
    }

    return 0;
#endif
}


