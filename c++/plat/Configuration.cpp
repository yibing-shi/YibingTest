#include "Configuration.h"
#include <stdio.h>
#include <stdlib.h>

namespace PLAT_7G {

Configuration::~Configuration()
{
    Config_File::iterator it = conf.begin();
    for(; it != conf.end(); ++it)
    {
        delete it->second;
    }
}

INT32 Configuration::load()
{
    FILE *conf_file = fopen(file_name.c_str(), "r");
    if (conf_file == NULL)
    {
        return -1;
    }
    
    char buf[1024];
    while (fgets(buf, sizeof(buf), conf_file) != NULL)
    {
        const char* line;
        const char* eq_pos;
        const char* line_end;
        analyze_line(buf, line, eq_pos, line_end);
        if (*line == '\0')
        {
            //This is a empty line
            continue;
        }

        if (*line == '#')
        {
            //This line is comments
            continue;
        }
        
        if (*line == '[' && *(line_end - 1) == ']')
        {
            //This line defines a section
            cur_sec_name = std::string(line + 1, line_end - 1);
            continue;
        }

        if (eq_pos == NULL)
        {
            //this is an invalid line, ignore it
            continue;
        }

        const char* p = eq_pos;
        while (p > line && (*(p - 1) == ' ' || *(p - 1) == '\t'))
            p--;
        std::string key = std::string(line, p - line);
        p = eq_pos;
        while (p > line && (*(p + 1) == ' ' || *(p + 1) == '\t'))
            p++;
        std::string val = std::string(p + 1, line_end - p - 1);
        Sector &sec = get_sector(cur_sec_name);
        sec.insert(Sector::value_type(key, val));
    }

    fclose(conf_file);
    
    return 0;
}

INT32 Configuration::get_int(const std::string& sector, 
                             const std::string& key, 
                             INT32 dft) const
{
    Config_File::const_iterator sec_it = conf.find(sector);
    if (sec_it == conf.end())
    {
        return dft;
    }

    Sector *sec = sec_it->second;
    Sector::const_iterator val_it = sec->find(key);
    if (val_it == sec->end())
    {
        return dft;
    }
    else
    {
        return atoi((val_it->second).c_str());
    }
}

const std::string& 
Configuration::get_string(const std::string& sector, 
                          const std::string& key, 
                          const std::string& dft) const
{
    Config_File::const_iterator sec_it = conf.find(sector);
    if (sec_it == conf.end())
    {
        return dft;
    }

    Sector *sec = sec_it->second;
    Sector::const_iterator val_it = sec->find(key);
    if (val_it == sec->end())
    {
        return dft;
    }
    else
    {
        return val_it->second;
    }
}

const char* Configuration::analyze_line(const char* line,  
                                        const char *&beg, 
                                        const char *&eq_pos, 
                                        const char *&end) 
{
    beg = line;
    while (*beg == ' ' || *beg == '\t')
        beg ++;

    eq_pos = NULL;
    const char* p = beg;
    for (; *p != '\0'; ++p)
    {
        if (eq_pos == NULL && *p == '=')
        {
            //take the first "=" as the splitter
            eq_pos = p;
        }
    }
    
    while ((p > beg)
            && (   *(p - 1) == ' ' 
                || *(p - 1) == '\t' 
                || *(p - 1) == '\r' 
                || *(p - 1) == '\n')
          )
    {
        p--;
    }
    end = p;

    return beg;
}

Configuration::Sector& 
Configuration::get_sector(const std::string &sec_name)
{
    Config_File::iterator it = conf.find(sec_name);
    if (it != conf.end())
    {
        return *(it->second);
    }
    else
    {
        Sector *sec = new Sector();
        conf[sec_name] = sec;
        return *sec;
    }
}

} //end of namespace

