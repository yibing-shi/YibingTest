// Message.cpp: implementation of the Message class.
//
//////////////////////////////////////////////////////////////////////

#include "Message.h"

namespace PLAT_7G{

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

const UINT32 Message::INVALID_TYPE = 0xFFFFFFFF;
const UINT32 Message::QUIT_MESSAGE = 0xFFFFFFFE;

Message::Message()
:type_(Message::INVALID_TYPE), body_(NULL), length_(0)
{

}

Message::Message(BYTE *body, UINT32 length)
:type_(Message::INVALID_TYPE), body_(body), length_(length)
{

}

Message::~Message()
{
    if (body_ != NULL)
    {
        delete[] body_;
    }
}


} //end of namespace

