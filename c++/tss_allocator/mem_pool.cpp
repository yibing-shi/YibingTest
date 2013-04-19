#include "mem_pool.h"
#include <cstddef>
#include <cstdlib>

mem_pool::mem_pool(size_t item_size_, size_t prefer_pool_size_)
    :item_size(item_size_), prefer_pool_size(prefer_pool_size_),
     pool_size(0), free_list(NULL)
{
}

mem_pool::~mem_pool()
{
    while (free_list)
    {
        item *p = free_list;
        free_list = p->next;
        free((void*)p);
    }
    pool_size = 0;
}

void* mem_pool::alloc()
{
    item *p;
    if (free_list)
    {
        p = free_list;
        free_list = p->next;
    }   
    else
    {   
        p = (item*) (::malloc(sizeof(item) + item_size));
    }   
    return p->mem;
}   

void mem_pool::dealloc(void *p) 
{   
    item *i = (item*)((char*)p - offsetof(item, mem));
    if (pool_size >= prefer_pool_size)
    {   
        ::free((void*) i);
    }   
    else
    {   
        i->next = free_list;
        free_list = i;
        ++pool_size;
    }   
}

