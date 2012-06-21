#include <string>
#include <iostream>


class A
{
public:
    A() : _id("I am A") {}
    A(const std::string& s) : _id(s) {}
    const std::string get_id() const
    {
        return _id;
    }

private:
    std::string _id;
};

class B
{
public:
    const std::string get_id() const
    {
        return "I am B";
    }

    operator A()
    {
        return A("I am B");
    }
};

void my_print(const A& a)
{
    std::cout << a.get_id() << std::endl;
}

int main()
{
    B b;
    my_print(b);

    return 0;
}

