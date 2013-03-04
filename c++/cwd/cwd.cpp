#include <unistd.h>
#include <stdlib.h>
#include <cstdio>

int main(int argc, char *argv[])
{
    char buf[1024];
    getcwd(buf, sizeof(buf));
    printf("getcwd return: %s\n", buf);

    char *envPwd = getenv("PWD");
    printf("getenv return: %s\n", envPwd);

    return 0;
}
