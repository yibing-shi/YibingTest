// Exception.cpp: implementation of the Exception class.
//
//////////////////////////////////////////////////////////////////////

#include "Exception.h"

namespace PLAT_7G{

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Exception::Exception()
:str_("")
{

}

Exception::Exception(std::string string_)
:str_(string_)
{

}

Exception::Exception(const Exception& rhs)
{
    str_ = rhs.str_;
}

Exception& Exception::operator=(const Exception& rhs)
{
    if (this == &rhs)
        return *this;
    this->str_ = rhs.str_;
    return *this;
}

Exception::~Exception()
{

}

} //end of namespace

