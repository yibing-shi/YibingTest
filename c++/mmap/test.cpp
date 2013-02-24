#include <cstdio>
#include <cstring>
#include <sys/mman.h>
#include <unistd.h>
#include <cstdlib>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <ctime>

int main(int argc, char *argv[])
{
    if (argc < 2)
    {
        printf("please speicfy a file\n");
        return -1;
    }

    int fd = open(argv[1], O_RDWR);
    if (fd == -1)
    {
        printf("open file %s failed\n", argv[1]);
        return -2;
    }

    struct stat stat_buf;
    if ((fstat(fd, &stat_buf)) == -1)
    {
        printf("failed to stat file %s\n", argv[1]);
        return -3;
    }

    char *buf = (char*)mmap(0, stat_buf.st_size, PROT_READ|PROT_WRITE, MAP_SHARED, fd, 0);
    if (buf == (char*) -1)
    {
        printf("failed to map file into memory. errno = %d\n", errno);
        return errno;
    }

    printf("===================================\n");

    const size_t BUF_SIZE = 1024 * 1024;
    srand(time(0));
    for (int i = 0; i < 9000; i++)
    {
        long total = 0;
        size_t offset = random() % (stat_buf.st_size - BUF_SIZE);
        for (int j = 0; j < BUF_SIZE; j++)
        {
            total += buf[offset + j];
            //buf[offset + j] = j % 256;
        }
        printf("Total from %016x is:%u\n", offset, total);
    }

    printf("===================================\n");
    getchar();
    return 0;
}
