// BaseCodec.h: interface for the BaseCodec class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_BASECODEC_H__E7FF9245_9996_41E5_980A_2951954A57A6__INCLUDED_)
#define AFX_BASECODEC_H__E7FF9245_9996_41E5_980A_2951954A57A6__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <string>
#include "config.h"
#include "Exception.h"

namespace PLAT_7G{

class BaseCodec  
{
public:
	BaseCodec();
	virtual ~BaseCodec();

public:
    static const BYTE ALIGH_LEFT;
    static const BYTE ALIGH_RIGHT;

public:
    static void encodeBYTE(BYTE *&buffer, UINT32 &bufLen, BYTE b);
    static void decodeBYTE(BYTE *&buffer, UINT32 &bufLen, BYTE &b);
    static void encodeUint16(BYTE *&buffer, UINT32 &bufLen, UINT16 i);
    static void decodeUint16(BYTE *&buffer, UINT32 &bufLen, UINT16 &i);
    static void encodeUint32(BYTE *&buffer, UINT32 &bufLen, UINT32 i);
    static void decodeUint32(BYTE *&buffer, UINT32 &bufLen, UINT32 &i);
    static void encodeUint64(BYTE *&buffer, UINT32 &bufLen, UINT64 i);
    static void decodeUint64(BYTE *&buffer, UINT32 &bufLen, UINT64 &i);
    static void encodeBYTEArray(BYTE *&buffer, 
                                UINT32 &bufLen, 
                                const BYTE *arr, 
                                UINT32 arrLen);
    static void decodeBYTEArray(BYTE *&buffer, 
                                UINT32 &bufLen, 
                                BYTE *arr, 
                                UINT32 arrLen);

    static void decodeCString(BYTE *&buffer, 
                              UINT32 &bufLen, 
                              UINT32 fieldLen, 
                              std::string &str);

    static void encodeCString(BYTE *&buffer, 
                              UINT32 &bufLen, 
                              const std::string& str, 
                              UINT32 width = 0,
                              BYTE align = ALIGH_LEFT);
    
    static void encodeCString(BYTE *&buffer, 
                              UINT32 &bufLen, 
                              const char* str, 
                              UINT32 width = 0,
                              BYTE align = ALIGH_LEFT);

    static void encodeCString(BYTE *&buffer, 
                              UINT32 &bufLen, 
                              const char* str, 
                              UINT32 strLen,
                              UINT32 width = 0,
                              BYTE align = ALIGH_LEFT);

};


} // end of namespace

#endif // !defined(AFX_BASECODEC_H__E7FF9245_9996_41E5_980A_2951954A57A6__INCLUDED_)

