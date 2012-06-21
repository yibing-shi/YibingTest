#ifndef __DELAY_THREAD_REMOVER__
#define __DELAY_THREAD_REMOVER__

#include <time.h>
#include <list>
#include "Singleton.h"
#include "ThreadMutex.h"
#include "Condition.h"
#include "Guard.h"
#include "Thread.h"


namespace PLAT_7G {


template<class T>
class DelayRemover_i : public Thread
{
    friend class Singleton<DelayRemover_i<T>, ThreadMutex>;
private:
    DelayRemover_i() 
        :started(FALSE), gc_list(), list_mutex()
    {
    }
    
    virtual ~DelayRemover_i() {}

    DelayRemover_i(const DelayRemover_i &rhs);
    DelayRemover_i& operator=(const DelayRemover_i &rhs);

public:
    virtual INT32 svc();

    INT32 add_item(T* item);

private:
    bool started;

    struct Item
    {
        time_t time;
        T *pointer;

        Item(time_t t, T* p) : time(t), pointer(p) {}
        ~Item() {}
        Item(const Item &rhs) : time(rhs.time), pointer(rhs.pointer) {}
        Item& operator=(const Item& rhs)
        {
            if (this == &rhs)
                return *this;
            time = rhs.time;
            pointer = rhs.pointer;
            return *this;
        }
    };
    std::list<Item> gc_list;
    ThreadMutex list_mutex;
};

template<class T>
INT32 DelayRemover_i<T>::add_item(T* p)
{
    time_t curTime;
    time(&curTime);

    {
        GUARD(ThreadMutex, list_mutex, g);
        Item item(curTime + 60, p);
        gc_list.push_back(item);

        if (!started)
        {
            start();
            started = TRUE;
        }
    }
    
    return 0;
}

template<class T>
INT32 DelayRemover_i<T>::svc()
{
    while (TRUE)
    {
        //sleep for 1 minute and then begin garbedge collection
        sleep(60);

        time_t curTime;
        time(&curTime);

        {
            GUARD(ThreadMutex, list_mutex, g);
            
            while (gc_list.size() > 0)
            {
                Item item = gc_list.front();
                if ( item.time > curTime )
                    break;
                gc_list.pop_front();
                delete item.pointer;
                LOG_DEBUG("DelayRemover: item %x has been deleted!", item.pointer);
            }
        }
        
    }
    
    return 0;
}

typedef Singleton<DelayRemover_i<Thread>, ThreadMutex> ThreadDelayRemover;

} 

#endif

