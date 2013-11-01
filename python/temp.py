#!/usr/bin/python

import json

if __name__=="__main__":
    m = {1:'a', 2:'b'}
    data = json.dumps(m, sort_keys=True, indent=4, separators=(',', ': '))
    print (data)
