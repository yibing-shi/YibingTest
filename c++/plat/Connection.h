#ifndef __PLAT_7G_CONNECTION_HEADER__
#define __PLAT_7G_CONNECTION_HEADER__

#include "Sock.h"

namespace PLAT_7G {


enum Conn_Status
{
    INITIALIZED = 0,
    CONNECTED = 1,
    DISCONNECTED = 2,
    CONNECTING = 3
};


enum Direction
{
    UNKNOWN = 0,
    INBOUND = 1,
    OUTBOUND = 2
};

class Connection
{
public:
    Connection()
        :sock_(), dir_(UNKNOWN), status_(INITIALIZED)
    {
    }

    Connection(Sock sock, Direction dir, Conn_Status status)
        :sock_(), dir_(dir), status_(status)
    {
    }

    ~Connection() {}

    Connection(const Connection& rhs)
        :sock_(rhs.sock_), dir_(rhs.dir_), status_(rhs.status_)
    {
    }

    Configuration & operator=(const Connection &rhs)
    {
        if (this == &rhs)
            return *this;
        sock_ = rhs.sock_;
        dir_ = rhs.dir_;
        status_ = rhs.status_;
        return *this;
    }

    Sock& get_sock() { return sock_; }
    void set_sock(const Sock& sock) { sock_ = sock; }
    Direction get_dir() { return dir_; }
    void set_dir(Direction dir) { dir_ = dir; }
    Conn_Status get_status() { return status_; }
    void set_status(Conn_Status status) { status_ = status; }

    INT32 close()
    {
        sock_.close();
    }

private:
    Sock sock_;
    Direction dir_;
    Conn_Status status_;
};


} //end of namespace

#endif

