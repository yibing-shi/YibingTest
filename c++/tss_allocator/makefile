OUTPUT = ${shell pwd}/tss_test
SOURCES = ${wildcard ${shell pwd}/*.cpp}
OBJS = ${SOURCES:.cpp=.o}

CFLAGS = -pthread -I../plat

ifeq (${optimize}, 1)
CFLAGS += -O3
else
CFLAGS += -g
endif

LD_FLAGS = ${CFLAGS} -L../lib -lplat

all: ${OBJS}
	g++ ${LD_FLAGS} -o ${OUTPUT} ${OBJS}  

%.o:%.cpp
	g++ -c ${CFLAGS} $< -o $@

clean:
	rm -rf ${OBJS} ${OUTPUT}

