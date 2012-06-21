#ifndef __CONFIGURATION_HEADER__
#define __CONFIGURATION_HEADER__

#include <string>
#include <map>
#include "config.h"

namespace PLAT_7G {

class Configuration
{
public:
    Configuration(const std::string &file) 
        : file_name(file), conf(), cur_sec_name("") {}

    ~Configuration();

    INT32 load();

    INT32 get_int(const std::string& sector, 
                  const std::string& key, 
                  INT32 dft) const;

    const std::string& get_string(const std::string& sector, 
                                  const std::string& key,
                                  const std::string& dft) const;

private:
    const std::string file_name;
    
    //A ini file contains several sectors, which contains key-value pairs
    typedef std::map<std::string, std::string> Sector;
    typedef std::map<std::string, Sector*> Config_File;
    Config_File conf;

    std::string cur_sec_name;

private:
    const char* analyze_line(const char *line, 
                             const char *&beg, 
                             const char *&eq_pos, 
                             const char *&end);

    Sector& get_sector(const std::string &sec_name);
};

} //end of namespace

#endif

