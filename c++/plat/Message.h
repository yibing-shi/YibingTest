// Message.h: int32erface for the Message class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_MESSAGE_H__79AA50F9_E6DD_4021_BF76_DF9343DB1452__INCLUDED_)
#define AFX_MESSAGE_H__79AA50F9_E6DD_4021_BF76_DF9343DB1452__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "config.h"

namespace PLAT_7G{

class Message  
{
public:
	Message();
	Message(BYTE *body, UINT32 length);
	virtual ~Message();

    void setType(UINT32 type)
    {
        type_ = type;
    }

    UINT32 getType() const
    {
        return type_;
    }
    
    void setBody(BYTE *body, UINT32 len)
    {
        body_ = body;
        length_ = len;
    }

    BYTE* getBody()
    {
        return body_;
    }

    UINT32 getLength()
    {
        return length_;
    }

    UINT32 getConnID()
    {
        return conn_id_;
    }

    void setConnID(UINT32 conn_id)
    {
        conn_id_ = conn_id;
    }

public:
    static const UINT32 INVALID_TYPE;

    static const UINT32 QUIT_MESSAGE;

private:
    UINT32 type_;
    UINT32 length_;
    BYTE* body_;

    UINT32 conn_id_;
};

} //end of namespace

#endif // !defined(AFX_MESSAGE_H__79AA50F9_E6DD_4021_BF76_DF9343DB1452__INCLUDED_)

