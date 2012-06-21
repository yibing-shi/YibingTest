#include <iostream>
#include <vector>
#include <algorithm>

class Item
{
public:
    Item (int i) : _i(i) {;}

    virtual void show() const
    {
        std::cout << "show ----" << std::endl;
    }

private:
    int _i;
};

int main()
{
    std::vector<Item> vec;
    for (int i = 0; i < 10; ++i)
    {
       vec.push_back(i); 
    }

    int even_num = 0;
    for_each(vec.begin(), vec.end(), [&even_num] (const Item &item) {
            item.show();
            even_num++;
            });

    std::cout << even_num << std::endl;

    return 0;
}

