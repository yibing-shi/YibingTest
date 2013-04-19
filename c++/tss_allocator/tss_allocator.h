#ifndef __TSS_ALLOCATOR_HEADER__
#define __TSS_ALLOCATOR_HEADER__

#include <limits>
#include <cstddef>
#include "mem_pool.h"


template <class T>
class tss_allocator
{
public:
    typedef T value_type;
    typedef T* pointer;
    typedef const T* const_pointer;
    typedef T& reference;
    typedef const T& const_reference;
    typedef size_t size_type;
    typedef ptrdiff_t difference_type;

    template <class U>
    struct rebind
    {
        typedef tss_allocator<U> other;
    };

    pointer address(reference value) const
    {
        return &value;
    }

    const_pointer address(const_reference value) const
    {
        return &value;
    }

    tss_allocator() throw()
    {
        init();
    }

    template <class V>
    tss_allocator(const tss_allocator<V>& rhs) throw()
    {
    }

    ~tss_allocator()
    {
    }

    size_type max_size() const throw()
    {
        return std::numeric_limits<size_type>::max() / sizeof(T);
    }

    //allocate but don't initialize num elements of type T
    pointer allocate (size_type num, const void* = 0)
    {
        size_type i = get_bucket_idx(num * sizeof(T));
        if (i >= BUCKETS_NUM)
        {
            return (pointer) (::operator new(num * sizeof(T)));
        }
        else
        {
            return (pointer) (buckets[i]->alloc());
        }
    }

    //initialize elements of allocated storage p with value value
    void construct (pointer p, const T& value)
    {
        new ((void*)p) T(value);
    }

    //destroy elements of initialized storage p 
    void destroy (pointer p)
    {
        p->~T();
    }

    //deallocate storage p of deleted elements
    void deallocate (pointer p, size_type num) 
    {
        size_type i = get_bucket_idx(num * sizeof(T));
        if (i >= BUCKETS_NUM)
        {
            delete p;
        }
        else
        {
            buckets[i]->dealloc(p);
        }
    }

private:
    static const size_type BUCKETS_NUM = 10;
    static __thread mem_pool** buckets;

    void init()
    {
        buckets = new mem_pool*[BUCKETS_NUM];
        size_type item_size = 8;
        size_type preferred_pool_len = 4096;
        for (int i = 0; i < BUCKETS_NUM; ++i)
        {
            buckets[i] = new mem_pool(item_size, preferred_pool_len);
            item_size << 1;
            preferred_pool_len >> 1;
        }
    }

    inline size_type get_bucket_idx(size_type size)
    {
        size_type i = 0;
        for (; size > 8; ++i)
        {
            size >>= 1;
        }
        return i;
    }
};

// all specializations of this allocator are interchangable
template <class T1, class T2>
bool operator == (const tss_allocator<T1>&, const tss_allocator<T2>&) throw()
{
    return true;
}

template <class T1, class T2>
bool operator != (const tss_allocator<T1>&, const tss_allocator<T2>&) throw()
{
    return false;
}

template <class T>
__thread mem_pool** tss_allocator<T>::buckets = NULL;

#endif

