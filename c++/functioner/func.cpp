#include <functional>
#include <string>
#include <vector>
#include <iostream>

class Test
{
public:
    Test(const std::string& s): _content(s) {}
    Test(const Test& rhs) : _content(rhs._content) {}
    Test& operator=(const Test& rhs)
    {
        if (this == &rhs)
            return *this;
        _content = rhs._content;
        return *this;
    }

    const std::string& get_content() const
    {
        return _content;
    }
private:
    std::string _content;
};

class PrintTest : public std::unary_function<const Test&, void>
{
public:
    void operator () (const Test& t)
    {
        std::cout << t.get_content() << std::endl;
    }
};

int main()
{
    std::vector<Test> v;
    v.reserve(3);
    v.push_back(Test("a"));
    v.push_back(Test("b"));
    v.push_back(Test("c"));

    for_each(v.begin(), v.end(), PrintTest());

    Test t("abcde");
    PrintTest p;
    p(t);
    PrintTest()(t);

    return 0;
}

