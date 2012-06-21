OUTPUT = ${shell pwd}/../lib/libplat.so

SOURCES = ${wildcard ${shell pwd}/*.cpp}
OBJECTS = ${SOURCES:%.cpp=%.o}

CFLAGS = -g -pthread -fPIC
LDFLAGS = -pthread

all:${OBJECTS}
	g++ -shared ${CFLAGS} -o ${OUTPUT} ${OBJECTS} ${LDFLAGS}

clean:
	rm -rf ${OUTPUT} ${OBJECTS}

${OBJECTS}:%.o:%.cpp
	g++ ${CFLAGS} -c -o $@ $<

