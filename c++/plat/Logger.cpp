// Logger.cpp: implementation of the Logger class.
//
//////////////////////////////////////////////////////////////////////

#include <cstdio>
#include <ctime>
#include <stdarg.h>
#include "Logger.h"


namespace PLAT_7G{

const UINT32 Logger_i::MAX_LOG_LEN = 1024;

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Logger_i::Logger_i()
:file_name(), logLevel(LEVEL_DEBUG), lock_(), cond_(), buffer_()
{

}

Logger_i::~Logger_i()
{

}

INT32 Logger_i::open(const std::string &file_name, LOG_LEVEL log_level)
{
    this->file_name = file_name;
    logLevel = log_level;
    return start();
}

void Logger_i::print_debug(const char *format, ...)
{
    if (logLevel > LEVEL_DEBUG)
        return;

    va_list va;
    va_start(va, format);
    print_i(LEVEL_DEBUG, format, va);
}

void Logger_i::print_info(const char *format, ...)
{
    if (logLevel > LEVEL_INFO)
        return;

    va_list va;
    va_start(va, format);
    print_i(LEVEL_INFO, format, va);
}

void Logger_i::print_warn(const char *format, ...)
{
    if (logLevel > LEVEL_WARN)
        return;

    va_list va;
    va_start(va, format);
    print_i(LEVEL_WARN, format, va);
}

void Logger_i::print_error(const char *format, ...)
{
    if (logLevel > LEVEL_ERROR)
        return;

    va_list va;
    va_start(va, format);
    print_i(LEVEL_ERROR, format, va);
}

#define TO_CHAR(x) ((x) < 0x0A ? ('0' + (x)) : ('A' + ((x) - 0x0A)))
void Logger_i::hexDump(const char* prefix, BYTE* stream, UINT32 len)
{
    if (logLevel > LEVEL_DEBUG)
        return;

    std::string result;

    result += prefix;
    result += '\n';

    result.reserve(len * 4);
    for (int i = 0; i < len; ++i)
    {
        if (i % 16 == 0)
        {
            result += '\t';
        }
        result += TO_CHAR(stream[i] >> 4);
        result += TO_CHAR(stream[i] & 0x0F);
        if ((i + 1) % 16 == 0)
        {
            result += '\n';
        }
        else if ((i + 1) % 8 == 0)
        {
            result += "    ";
        }
        else
        {
            result += ' ';
        }
    }

    print_i(LEVEL_DEBUG, "%s", result.c_str());
}

void Logger_i::print_i(BYTE level, const char *format, ...)
{
    va_list va;
    va_start(va, format);
    print_i(level, format, va);
}

void Logger_i::print_i(BYTE level, const char *format, va_list va)
{
    char *logBuffer = new char[MAX_LOG_LEN];
    char *p = logBuffer;

    time_t curTick;
    time(&curTick);
    struct tm *ltime = localtime(&curTick);
    int offset = sprintf(p, 
                         "[%04d-%02d-%02d %02d:%02d:%02d]", 
                         ltime->tm_year + 1900,
                         ltime->tm_mon + 1,
                         ltime->tm_mday,
                         ltime->tm_hour,
                         ltime->tm_min,
                         ltime->tm_sec);
    p += offset;

    offset = sprintf(p, 
                     "[pid:%u tid:%u]", 
                     get_process_id(),
                     get_thread_id());
    p += offset;

    switch (level)
    {
    case LEVEL_DEBUG:
        offset = sprintf(p, "%s", " [DEBUG]: ");
        break;
    case LEVEL_INFO:
        offset = sprintf(p, "%s", " [INFO]: ");
        break;
    case LEVEL_WARN:
        offset = sprintf(p, "%s", " [WARN]: ");
        break;
    case LEVEL_ERROR:
        offset = sprintf(p, "%s", " [ERROR]: ");
        break;
    default:
        offset = 0;
    }
    p += offset;

    vsnprintf(p, MAX_LOG_LEN, format, va);

    lock_.lock();
    buffer_.push_back(logBuffer);
    cond_.signal();
    lock_.unlock();
}


INT32 Logger_i::svc()
{
    FILE *file;
    if ((file = ::fopen(file_name.c_str(), "a+")) == NULL)
    {
        return -1;
    }

    while (1)
    {
        lock_.lock();
        while (buffer_.size() == 0)
        {
            cond_.wait(lock_);
        }
        char * log = buffer_.front();
        buffer_.pop_front();
        lock_.unlock();

        ::fprintf(file, "%s\n", log);
        ::fflush(file);
        delete[] log;
    }

    return 0;
}


} //end of namespace

