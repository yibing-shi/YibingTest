#ifndef __MD5_HEADER__
#define __MD5_HEADER__

#ifdef  __cplusplus
extern "C" {
#endif /* #ifdef  __cplusplus */

#ifndef _LRAD_MD5_H
#define _LRAD_MD5_H

typedef unsigned char *POINTER;
typedef const unsigned char *CONSTPOINTER;
typedef unsigned int UINT4;

/* MD5 context. */
typedef struct 
{
  UINT4 state[4];                                   /* state (ABCD) */
  UINT4 count[2];        /* number of bits, modulo 2^64 (lsb first) */
  unsigned char buffer[64];                         /* input buffer */
}MD5_CTX;

void      MD5Init(MD5_CTX*);
void      MD5Update(MD5_CTX *, const unsigned char *, unsigned int);
void      MD5Final(unsigned char [16], MD5_CTX *);
void      librad_md5_calc(unsigned char *output, unsigned char *input,unsigned int inlen);
void      lrad_hmac_md5(const unsigned char *text, int text_len,
	                    const unsigned char *key, int key_len,unsigned char *digest);
#endif /* _LRAD_MD5_H */

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif /* __MD5_HEADER__ */

