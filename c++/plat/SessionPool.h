#ifndef __SESSION_POOL_HEADER__
#define __SESSION_POOL_HEADER__

#include "Session.h"
#include <list>
#include <map>

using std::list;
using std::map;

template<class T>
class SessionPool
{
public:
    SessionPool();
    ~SessionPool();

    Session<T>* get();
    Session<T>* find(int idx);
    int release(int idx);

public:
    enum {
        SUCCESS = 0,
        CANNOT_FIND_ITEM
    };

private:
    int size;

    typedef list<int> ListType;
    ListType freeList;

    typedef map<int, Session<T>*> MapType;
    MapType dataMap;

private:
    static const int ENLARGE_SIZE;
    void enlarge();
};

template<class T>
const int SessionPool<T>::ENLARGE_SIZE = 100;

template<class T>
SessionPool<T>::SessionPool()
{
    size = 0;
}

template<class T>
SessionPool<T>::~SessionPool()
{
    typename MapType::iterator it = dataMap.begin();
    for(; it != dataMap.end(); ++it) 
    {
        if (it->second != NULL)
        {
            delete(it->second);
        }
    }
}

template<class T>
Session<T>* SessionPool<T>::get()
{
    if (freeList.size() == 0)
    {
        enlarge();
    }

    int idx = freeList.front();
    freeList.pop_front();
    typename MapType::iterator it = dataMap.find(idx);
    if (it == dataMap.end())
    {
        return NULL;
    }
    else
    {
        return it->second;
    }
}

template<class T>
Session<T>* SessionPool<T>::find(int idx)
{
    typename MapType::iterator it = dataMap.find(idx);
    if (it == dataMap.end())
    {
        return NULL;
    }
    else
    {
        return it->second;
    }
}

template<class T>
int SessionPool<T>::release(int idx)
{
    typename MapType::iterator it = dataMap.find(idx);
    if (it == dataMap.end())
    {
        return CANNOT_FIND_ITEM;
    }

    freeList.push_front(idx);
    return SUCCESS;
}

template<class T>
void SessionPool<T>::enlarge()
{
    for (int i = 0; i < ENLARGE_SIZE; ++i)
    {
        Session<T> *session = new Session<T>(size + i);
        dataMap.insert(std::pair<int, Session<T>*>(size + i, session));
        freeList.push_back(size + i);
    }

    size += ENLARGE_SIZE;
}

#endif

