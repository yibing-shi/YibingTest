// Logger.h: int32erface for the Logger class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_LOGGER_H__4F86021E_741E_4BB2_A0BC_F102455CFD0F__INCLUDED_)
#define AFX_LOGGER_H__4F86021E_741E_4BB2_A0BC_F102455CFD0F__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <list>
#include <string>
#include "config.h"
#include "ThreadMutex.h"
#include "Condition.h"
#include "Thread.h"
#include "Guard.h"
#include "Singleton.h"
#include "stdarg.h"

namespace PLAT_7G{

enum LOG_LEVEL
{
    LEVEL_DEBUG = 1,
    LEVEL_INFO = 2,
    LEVEL_WARN = 3,
    LEVEL_ERROR = 4
};

class Logger_i : public Thread
{
public:
	Logger_i();
	virtual ~Logger_i();

protected:
    virtual INT32 svc();

public:
    INT32 open(const std::string &file_name, 
        LOG_LEVEL log_level = LEVEL_DEBUG);
    void print_debug(const char *format, ...);
    void print_info(const char *format, ...);
    void print_warn(const char *format, ...);
    void print_error(const char *format, ...);
    void hexDump(const char* prefix, BYTE* stream, UINT32 len);

private:
    void print_i(BYTE level, const char *format, ...);
    void print_i(BYTE level, const char *format, va_list va);

protected:
    static const UINT32 MAX_LOG_LEN;

private:
    std::string file_name;
    BYTE logLevel;
    ThreadMutex lock_;
    Condition<ThreadMutex> cond_;
    std::list<char *> buffer_;

};

typedef Singleton<Logger_i, ThreadMutex> Logger;

#define LOG_DEBUG PLAT_7G::Logger::instance()->print_debug
#define LOG_INFO PLAT_7G::Logger::instance()->print_info
#define LOG_WARN PLAT_7G::Logger::instance()->print_warn
#define LOG_ERROR PLAT_7G::Logger::instance()->print_error
#define HEX_DUMP PLAT_7G::Logger::instance()->hexDump

} //end of namespace

#endif // !defined(AFX_LOGGER_H__4F86021E_741E_4BB2_A0BC_F102455CFD0F__INCLUDED_)

