// Guard.h: interface for the Guard class.
//
//////////////////////////////////////////////////////////////////////

#ifndef __GUARD_HEADER__
#define __GUARD_HEADER__

template <class LOCK_TYPE>
class Guard  
{
public:
	Guard(LOCK_TYPE& lock)
        :rhs_(lock)
    {
        rhs_.lock();
    }

	virtual ~Guard()
    {
        rhs_.unlock();
    }

private:
    LOCK_TYPE &rhs_;
};

#define GUARD(LockType, LockObj, GuardObjName) Guard<LockType> GuardObjName(LockObj)

#endif // __GUARD_HEADER__

