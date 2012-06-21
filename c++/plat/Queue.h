// Queue.h: int32erface for the Queue class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_QUEUE_H__36B35622_D76D_4E63_836C_4350340E3B11__INCLUDED_)
#define AFX_QUEUE_H__36B35622_D76D_4E63_836C_4350340E3B11__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <list>
#include "config.h"


namespace PLAT_7G{

template<class ITEM, class LOCK, class CONDITION>
class Queue  
{
public:
	Queue();
	virtual ~Queue();

public:
    void putHead(ITEM item);
    void putTail(ITEM item);
    ITEM getHead();
    ITEM getTail();

private:
    std::list<ITEM> list_;
    LOCK lock_;
    CONDITION cond_;
};

template<class ITEM, class LOCK, class CONDITION>
Queue<ITEM, LOCK, CONDITION>::Queue()
:list_(), lock_(), cond_()
{
    list_.clear();
}

template<class ITEM, class LOCK, class CONDITION>
Queue<ITEM, LOCK, CONDITION>::~Queue()
{

}

template<class ITEM, class LOCK, class CONDITION>
void Queue<ITEM, LOCK, CONDITION>::putHead(ITEM item)
{
    lock_.lock();
    list_.push_front(item);
    cond_.signal();
    lock_.unlock();
}

template<class ITEM, class LOCK, class CONDITION>
void Queue<ITEM, LOCK, CONDITION>::putTail(ITEM item)
{
    lock_.lock();
    list_.push_back(item);
    cond_.signal();
    lock_.unlock();
}

template<class ITEM, class LOCK, class CONDITION>
ITEM Queue<ITEM, LOCK, CONDITION>::getHead()
{
    lock_.lock();
    while (list_.empty())
    {
        cond_.wait(lock_);
    }

    ITEM item = list_.front();
    list_.pop_front();
    
    lock_.unlock();
    return item;
}

template<class ITEM, class LOCK, class CONDITION>
ITEM Queue<ITEM, LOCK, CONDITION>::getTail()
{
    lock_.lock();
    while (list_.empty())
    {
        cond_.wait(lock_);
    }

    ITEM item = list_.back();
    list_.pop_back();
    
    lock_.unlock();
    return item;
}


} //end of namespace

#endif // !defined(AFX_QUEUE_H__36B35622_D76D_4E63_836C_4350340E3B11__INCLUDED_)

