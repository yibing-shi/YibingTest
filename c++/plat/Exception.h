// Exception.h: int32erface for the Exception class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_EXCEPTION_H__389C20C5_0351_4483_A28D_4798D48D730B__INCLUDED_)
#define AFX_EXCEPTION_H__389C20C5_0351_4483_A28D_4798D48D730B__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <string>
#include "config.h"

namespace PLAT_7G{

class Exception  
{
public:
    Exception();
    Exception(std::string str_);
    Exception(const Exception& rhs);
    Exception& operator=(const Exception& rhs);
	virtual ~Exception();

private:
    std::string str_;
};

} //end of namespace

#endif // !defined(AFX_EXCEPTION_H__389C20C5_0351_4483_A28D_4798D48D730B__INCLUDED_)

