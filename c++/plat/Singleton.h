// Singleton.h: interface for the Singleton class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SINGLETON_H__5CE6953F_CC61_4197_8BF7_CE77556560ED__INCLUDED_)
#define AFX_SINGLETON_H__5CE6953F_CC61_4197_8BF7_CE77556560ED__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


namespace PLAT_7G{

#include "config.h"

template<class TYPE, class LOCK>
class Singleton  
{
protected:
	Singleton();
	virtual ~Singleton();

    Singleton(const Singleton&);
    Singleton& operator=(const Singleton&);

private:
    static TYPE *instance_;
    static LOCK lock_;

public:
    static TYPE* instance();
};


template<class TYPE, class LOCK>
Singleton<TYPE, LOCK>::Singleton()
{

}

template<class TYPE, class LOCK>
Singleton<TYPE, LOCK>::~Singleton()
{

}

template<class TYPE, class LOCK>
TYPE* Singleton<TYPE, LOCK>::instance_ = NULL;

template<class TYPE, class LOCK>
LOCK Singleton<TYPE, LOCK>::lock_;

template<class TYPE, class LOCK>
TYPE* Singleton<TYPE, LOCK>::instance()
{
    if (instance_ == NULL)
    {
        lock_.lock();
        if (instance_ == NULL)
        {
            instance_ = new TYPE();
        }
        lock_.unlock();
    }

    return instance_;
}

} //end of namespace

#endif // !defined(AFX_SINGLETON_H__5CE6953F_CC61_4197_8BF7_CE77556560ED__INCLUDED_)

