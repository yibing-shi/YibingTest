#ifndef __SESSION_HEADER__
#define __SESSION_HEADER__

template <class T>
class Session
{
public:
    Session(int index);
    ~Session();

    int getIndex() { return index; }

public:
    T* operator->() { return &svcData; }
    const T* operator->() const { return &svcData; }
    T& operator*() { return svcData; }
    const T& operator*() const { return svcData; }

    operator T() { return svcData; }
    operator const T() const { return svcData; }

private:
    int index;
    T svcData;
};

template<class T>
Session<T>::Session(int index)
{
    this->index = index;
}

template<class T>
Session<T>::~Session()
{
}

#endif

