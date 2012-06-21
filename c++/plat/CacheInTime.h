#ifndef __CACHE_IN_TIME_HEADER__
#define __CACHE_IN_TIME_HEADER__

#include <string>
#include <map>
#include <list>
#include <ctime>
#include <ace/Guard_T.h>

using std::string;
using std::map;
using std::list;

template<class LOCK_TYPE>
class CacheInTime
{
public:
    enum Action
    {
        FORWARD_SMS = 0,
        VALIDATION_CODE = 1,
        CONFIRM_SMS = 2,
        ACTION_TYPE_COUNT = 3
    };
    
private:
    struct Counter
    {
        char counter_arr[ACTION_TYPE_COUNT];

        Counter()
        {
            for (int i = 0; i < ACTION_TYPE_COUNT; ++i)
            {
                counter_arr[i] = 0;
            }
        }
    };

    struct Record
    {
        time_t time;
        string user;
        Action action;
    };

    typedef map<string, Counter> MapType;
    MapType counter_map;

    typedef list<Record> ListType;
    ListType record_list;

    const unsigned int time_out;
    const unsigned int max_count;

public:
    CacheInTime(const unsigned int timeOut, const unsigned int maxCount)
        :time_out(timeOut), max_count(maxCount)
    {
        
    }
    
    int init();

    int check_and_update(const string &user, Action action);

private:
    void clear_old_records(time_t curTime);

    LOCK_TYPE lock_;
};

template<class LOCK_TYPE>
int CacheInTime<LOCK_TYPE>::init()
{
    return 0;
}

template<class LOCK_TYPE>
int CacheInTime<LOCK_TYPE>::check_and_update(
    const string &user, Action action)
{
    ACE_GUARD_RETURN(LOCK_TYPE, g, lock_, -1);
    
    time_t curTime;
    time(&curTime);
    clear_old_records(curTime);
    
    typename MapType::iterator it = counter_map.find(user);
    if (it == counter_map.end())
    {            
        Counter counter;
        counter.counter_arr[action]++;
        counter_map[user] = counter;
    }
    else
    {
        if((it->second).counter_arr[action] >= max_count)
        {
            return -1;
        }
        else
        {
            (it->second).counter_arr[action] ++;
        }
    }
    
    Record rec;
    rec.time = curTime;
    rec.user = user;
    rec.action = action;
    record_list.push_back(rec);
    
    return 0;
}

template<class LOCK_TYPE>
void CacheInTime<LOCK_TYPE>::clear_old_records(time_t curTime)
{
    typename ListType::iterator it = record_list.begin();
    while (curTime - it->time > time_out)
    {
        typename MapType::iterator map_it = counter_map.find(it->user);
        if (map_it != counter_map.end())
        {
            (map_it->second).counter_arr[it->action]--;
            
            int i;
            for (i = 0; i < ACTION_TYPE_COUNT; ++i)
            {
                if ((map_it->second).counter_arr[i] != 0)
                    break;
            }
            if (i == ACTION_TYPE_COUNT)
                counter_map.erase(map_it);
        }
        
        it = record_list.erase(it);
    }
}
    
#endif

