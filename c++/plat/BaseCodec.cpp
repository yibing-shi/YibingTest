// BaseCodec.cpp: implementation of the BaseCodec class.
//
//////////////////////////////////////////////////////////////////////

#include "BaseCodec.h"
#include <string.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////


namespace PLAT_7G {


const BYTE BaseCodec::ALIGH_LEFT    = 0;
const BYTE BaseCodec::ALIGH_RIGHT   = 1;


BaseCodec::BaseCodec()
{

}

BaseCodec::~BaseCodec()
{

}


void BaseCodec::encodeBYTE(BYTE *&buffer, UINT32 &bufLen, BYTE b)
{
    if (bufLen < sizeof(BYTE)) 
    {
        throw Exception("Codec error!");
    }

    *buffer = b;
    ++buffer;
    --bufLen;
}

void BaseCodec::decodeBYTE(BYTE *&buffer, UINT32 &bufLen, BYTE &b)
{
    if (bufLen < sizeof(BYTE))
    {
        throw Exception("Codec error!");
    }

    b = *buffer;
    ++buffer;
    --bufLen;
}

void BaseCodec::encodeUint16(BYTE *&buffer, UINT32 &bufLen, UINT16 i)
{
    if (bufLen < sizeof(UINT16))
    {
        throw Exception("Codec error!");
    }

    buffer[0] = (BYTE)(i >> 8);
    buffer[1] = (BYTE)(i & 0x00FF);
    buffer += sizeof(UINT16);
    bufLen -= sizeof(UINT16);
}

void BaseCodec::decodeUint16(BYTE *&buffer, UINT32 &bufLen, UINT16 &i)
{
    if (bufLen < sizeof(UINT16))
    {
        throw Exception("Codec error!");
    }

    i = (UINT16)((buffer[0] << 8) + buffer[1]);
    buffer += sizeof(UINT16);
    bufLen -= sizeof(UINT16);
}

void BaseCodec::encodeUint32(BYTE *&buffer, UINT32 &bufLen, UINT32 i)
{
    if (bufLen < sizeof(UINT32))
    {
        throw Exception("Codec error!");
    }

    buffer[0] = (BYTE)(i >> 24);
    buffer[1] = (BYTE)((i >> 16) & 0x000000FF);
    buffer[2] = (BYTE)((i >> 8) & 0x000000FF);
    buffer[3] = (BYTE)(i & 0x000000FF);
    buffer += sizeof(UINT32);
    bufLen -= sizeof(UINT32);
}

void BaseCodec::decodeUint32(BYTE *&buffer, UINT32 &bufLen, UINT32 &i)
{
    if (bufLen < sizeof(UINT32))
    {
        throw Exception("Codec error!");
    }

    i = (buffer[0] << 24) + (buffer[1] << 16) + (buffer[2] << 8) + buffer[3];
    buffer += sizeof(UINT32);
    bufLen -= sizeof(UINT32);
}

void BaseCodec::encodeUint64(BYTE *&buffer, UINT32 &bufLen, UINT64 i)
{
    if (bufLen < sizeof(UINT64))
    {
        throw Exception("Codec error!");
    }

    buffer[0] = (BYTE)(i >> 56);
    buffer[1] = (BYTE)((i >> 48) & 0x00000000000000FF);
    buffer[2] = (BYTE)((i >> 40) & 0x00000000000000FF);
    buffer[3] = (BYTE)((i >> 32) & 0x00000000000000FF);
    buffer[4] = (BYTE)((i >> 24) & 0x00000000000000FF);
    buffer[5] = (BYTE)((i >> 16) & 0x00000000000000FF);
    buffer[6] = (BYTE)((i >> 8) & 0x00000000000000FF);
    buffer[7] = (BYTE)(i & 0x00000000000000FF);
    buffer += sizeof(UINT64);
    bufLen -= sizeof(UINT64);
}

void BaseCodec::decodeUint64(BYTE *&buffer, UINT32 &bufLen, UINT64 &i)
{
    if (bufLen < sizeof(UINT64))
    {
        throw Exception("Codec error!");
    }

    i = buffer[0];
    for (int j = 1; j < sizeof(i); ++j)
    {
        i <<= 8;
        i += buffer[j];
    }
    buffer += sizeof(UINT64);
    bufLen -= sizeof(UINT64);
}


void BaseCodec::encodeBYTEArray(BYTE *&buffer, UINT32 &bufLen, const BYTE *arr, UINT32 arrLen)
{
    if (bufLen < arrLen)
    {
        throw Exception("Codec error!");
    }

    ::memcpy(buffer, arr, arrLen);
    buffer += arrLen;
    bufLen -= arrLen;
}

void BaseCodec::decodeBYTEArray(BYTE *&buffer, UINT32 &bufLen, BYTE *arr, UINT32 arrLen)
{
    if (bufLen < arrLen)
    {
        throw Exception("Codec error!");
    }

    ::memcpy(arr, buffer, arrLen);
    buffer += arrLen;
    bufLen -= arrLen;
}

void BaseCodec::decodeCString(BYTE *&buffer, 
                              UINT32 &bufLen, 
                              UINT32 fieldLen, 
                              std::string &str)
{
    if (bufLen < fieldLen)
    {
        throw Exception("Codec error!");
    }
    
    UINT32 p = 0, q = fieldLen - 1;

    while (buffer[p] == '0')
        ++p;

    while (q > p && buffer[q] == '\0')
        --q;

    if (q == p && buffer[p] == '\0')
    {
        str = "";
    }
    else
    {
        str.assign((char *)buffer + p, q - p + 1);
    }
    
    buffer += fieldLen;
    bufLen -= fieldLen;
}

void BaseCodec::encodeCString(BYTE *&buffer, 
                              UINT32 &bufLen, 
                              const std::string& str, 
                              UINT32 width,
                              BYTE align)
{
    encodeCString(buffer, bufLen, str.c_str(), str.length(), width, align);
}

void BaseCodec::encodeCString(BYTE *&buffer, 
                              UINT32 &bufLen, 
                              const char* str,
                              UINT32 width,
                              BYTE align)
{
    encodeCString(buffer, bufLen, str, strlen(str), width, align);
}

void BaseCodec::encodeCString(BYTE *&buffer, 
                              UINT32 &bufLen, 
                              const char* str, 
                              UINT32 strLen,
                              UINT32 width,
                              BYTE align)
{
    if (width != 0 && width < strLen)
    {
        width = strLen;
    }

    if (bufLen < strLen || bufLen < width)
    {
        throw Exception("Codec error!");
    }

    if (align == ALIGH_LEFT)
    {
        ::memcpy(buffer, str, strLen);
        if (width > strLen)
            ::memset(buffer + strLen, 0, width - strLen);
    }
    else
    {
        if (width > strLen)
            ::memset(buffer, '0', width - strLen);
        ::memcpy(buffer + width - strLen, str, strLen);
    }
    
    buffer += width > strLen ? width : strLen;
    bufLen -= width > strLen ? width : strLen;
}


} //end of namespace

