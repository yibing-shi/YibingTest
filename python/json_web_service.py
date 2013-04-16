#!/usr/bin/python

import json
import requests

body = {}
body['entityId'] = '0'
body['categoryId'] = '0'
body['partnerId'] = '0,-1'
body['areaId'] = ''
body['countryCode'] = '99'
body['startDateTime'] = '2012050100'
body['endDateTime'] = '2012060100'
body['timeGroupCode'] = '30'
body['entityTypeCode'] = 'publication'
body['regionId'] = '0,1,2,3,4,5,6'
body['metric'] = '1,2,3,4,5'
body['avg'] = True
body['orderBy'] = True

request = {}
request['type'] = 'TRAFFIC_COMBINED_LOAD_REQUEST'
request['body'] = {'trafficRequests' : [body, body] }

url = "http://192.168.100.207:8080/data-manager/1.1.0/services/traffic"
headers = {'Content-Type': 'application/json', 'Accept': 'application/json'}
response = requests.post(url, data=json.dumps(request), headers=headers)

print(json.dumps(request))
print(response.text)

