#include <iostream>

class base;

class base_lock
{
friend class base;
private:
    base_lock() {}
    base_lock(const base_lock &) {}
};

class base : public virtual base_lock
{
public:
    base() {}
    base(const base&) {}
    ~base() {}
};

class derived: public base
{
};

int main()
{
    derived d;
    return 0;
}

