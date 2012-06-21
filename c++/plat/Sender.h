// Sender.h: interface for the Sender class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SENDER_H__6835E6E6_B03C_4C4D_90D5_D2E12DD5FBCB__INCLUDED_)
#define AFX_SENDER_H__6835E6E6_B03C_4C4D_90D5_D2E12DD5FBCB__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "config.h"
#include "Sock.h"
#include "Task.h"

namespace PLAT_7G{

class Sender : public Task  
{
public:
	Sender(const Sock &socket);
	virtual ~Sender();

public:
    void sendMsg(BYTE *msg, UINT32 length);
    virtual INT32 onMessage(Message *msg);

private:
    Sock sock_;
};

} //end of namespace

#endif // !defined(AFX_SENDER_H__6835E6E6_B03C_4C4D_90D5_D2E12DD5FBCB__INCLUDED_)

