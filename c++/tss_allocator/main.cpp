#include <unistd.h>
#include <string>
#include <iostream>
#include "tss_allocator.h"
#include "../plat/Thread.h"

using std::cout;
using std::cerr;
using std::endl;
using std::ostream;
using PLAT_7G::Thread;

typedef std::basic_string< char, std::char_traits<char>, tss_allocator<char> > \
        my_string;


ostream& operator<<(ostream& stream, const my_string& str)
{
    stream << std::hex << (void*)str.c_str() << str.c_str();
    return stream;
}

class TestThread : public Thread
{
protected:
    virtual INT32 svc();
};

INT32 TestThread::svc()
{
    my_string str = "abcd";
    for (int i = 0; i < 100000; ++i)
    {
        str += "abcd";
    }
    str = str.substr(0, 1024);
}

int main()
{
    my_string str = "abcd";
    cout << str << endl;
    str.assign("abcdefg");
    cout << str << endl;

    TestThread t1, t2;
    t1.start();
    t2.start();

    sleep(20);

    return 0;
}

