#ifndef __MEM_POOL_HEADER__
#define __MEM_POOL_HEADER__

#include <unistd.h>

class mem_pool
{
public:
    mem_pool(size_t item_size_, size_t prefer_pool_size_);

    ~mem_pool();

    void* alloc();

    void dealloc(void *p);

private:
    mem_pool(const mem_pool&);
    mem_pool& operator=(const mem_pool&);

private:
    const size_t item_size;
    const size_t prefer_pool_size;
    size_t pool_size;

    struct item
    {
        struct item* next;
        char mem[];
    };

    item* free_list;
};

#endif

