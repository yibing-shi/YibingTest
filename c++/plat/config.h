#ifndef __PLAT_7G_CONFIG_HEADER__
#define __PLAT_7G_CONFIG_HEADER__

#ifdef WIN32

typedef unsigned char       BYTE;
typedef char                CHAR;
typedef short               INT16;
typedef unsigned short      UINT16;
typedef int                 INT32;
typedef unsigned int        UINT32;
typedef __int64             INT64;
typedef unsigned __int64    UINT64;

#define vsnprintf _vsnprintf
#define snprintf _snprintf

#define EWOULDBLOCK         WSAEWOULDBLOCK

#else //WIN32

typedef unsigned char       BYTE;
typedef char                CHAR;
typedef short               INT16;
typedef unsigned short      UINT16;
typedef int                 INT32;
typedef unsigned int        UINT32;
#ifdef BIT64
typedef long                INT64;
typedef unsigned long       UINT64;
#else  //BIT64
typedef long long           INT64;
typedef unsigned long long  UINT64;
#endif  //BIT64

typedef int                 HANDLE;

#define INVALID_SOCKET      (-1)
#define SOCKET_ERROR        (-1)


INT32 set_nonblock(HANDLE handle);

#endif  //WIN32


#ifndef NULL
#define NULL    0
#endif

#ifndef TRUE
#define TRUE    1
#endif

#ifndef FALSE
#define FALSE   0
#endif


INT32 get_os_err();
UINT32 get_process_id();
UINT32 get_thread_id();

#endif

