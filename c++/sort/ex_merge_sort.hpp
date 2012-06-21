#include <list>
#include <iostream>

template<class T>
class merge_sort
{
public:
    static void sort(std::list<T>& input);
private:
    static void split(std::list<T>& input, std::list<T>& sub1, std::list<T>& sub2);
    static void merge(std::list<T>& input1, std::list<T>& input2, size_t merge_depth, std::list<T>& out1, std::list<T>& out2);

};

template<class T>
static void swap(T *l1, T *l2)
{
    T t = *l1;
    *l1 = *l2;
    *l2 = t;
}

template<class T>
void print(const std::list<T> &list)
{
    for (typename std::list<T>::const_iterator it = list.begin(); it != list.end(); ++it)
    {
        std::cout << *it << " ";
    }
    std::cout << std::endl;
}

template<class T>
void merge_sort<T>::split(std::list<T>& input, std::list<T>& sub1, std::list<T>& sub2)
{
    std::list<T> *current = &sub1;
    while (input.size() >= 2)
    {
        T a1 = input.front();
        input.pop_front();
        T a2 = input.front();
        input.pop_front();
        if (a1 <= a2)
        {
            current->push_back(a1);
            current->push_back(a2);
        }
        else
        {
            current->push_back(a2);
            current->push_back(a1);
        }
        if (current == &sub1)
            current = &sub2;
        else
            current = &sub1;
    }
    if (input.size() == 1)
    {
        current->push_back(input.front());
        input.pop_front();
    }
}

template<class T>
void merge_sort<T>::merge(std::list<T>& input1, std::list<T>& input2, size_t merge_depth, std::list<T>& out1, std::list<T>& out2)
{
    size_t merged1 = 0, merged2 = 0;
    std::list<T> *current = &out1;
    while (input1.size() > 0 && input2.size() > 0)
    {
        if (input1.front() <= input2.front())
        {
            current->push_back(input1.front());
            input1.pop_front();
            merged1++;
        }
        else
        {
            current->push_back(input2.front());
            input2.pop_front();
            merged2++;
        }

        if (merged1 == merge_depth)
        {
            while(input2.size() > 0 && merged2 < merge_depth)
            {
                current->push_back(input2.front());
                input2.pop_front();
                merged2++;
            }
        }

        if (merged2 == merge_depth)
        {
            while(input1.size() > 0 && merged1 < merge_depth)
            {
                current->push_back(input1.front());
                input1.pop_front();
                merged1++;
            }
        }

        if (merged1 == merge_depth || merged2 == merge_depth)
        {
            merged1 = merged2 = 0;
            if (current == &out1)
                current = &out2;
            else
                current = &out1;
        }
    }
    while (input1.size() > 0)
    {
        current->push_back(input1.front());
        input1.pop_front();
    }
    while (input2.size() > 0)
    {
        current->push_back(input2.front());
        input2.pop_front();
    }
}

template<class T> void merge_sort<T>::sort(std::list<T>& input)
{
    size_t length = input.size();
    if (length <= 1)
        return;

    std::list<T> sub1, sub2, sub3, sub4;
    split(input, sub1, sub2);
    print(sub1);
    print(sub2);

    size_t merge_depth = 2;
    std::list<T> *in1 = &sub1, *in2 = &sub2;
    std::list<T> *out1 = &sub3, *out2 = &sub4;
    while (merge_depth * 2 < length)
    {
        std::cout << "----------------------" << std::endl;
        print(*in1);
        print(*in2);
        merge(*in1, *in2, merge_depth, *out1, *out2);
        std::cout << "----------------------" << std::endl;
        print(*in1);
        print(*in2);
        std::cout << "----------------------" << std::endl;
        print(*out1);
        print(*out2);
        swap(&in1, &out1);
        swap(&in2, &out2);
        std::cout << "----------------------" << std::endl;
        print(*in1);
        print(*in2);
        merge_depth *= 2;
    }

    print(*in1);
    print(*in2);
    input.clear();
    while(in1->size() > 0 && in2->size() > 0)
    {
        if (in1->front() <= in2->front())
        {
            input.push_back(in1->front());
            in1->pop_front();
        }
        else
        {
            input.push_back(in2->front());
            in2->pop_front();
        }
    }
    while (in1->size() > 0)
    {
            input.push_back(in1->front());
            in1->pop_front();
    }
    while (in2->size() > 0)
    {
        input.push_back(in2->front());
        in2->pop_front();
    }
}

int main()
{
    int a1[] = {5, 4, 3, 2, 1};
    std::list<int> l1(a1, a1 + sizeof(a1)/sizeof(int));
    print(l1);
    merge_sort<int>::sort(l1);
    print(l1);
}
