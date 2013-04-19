#!/usr/bin/python

import json
import requests
import queue
import threading
import time

def construct_ws_request():
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

    return request


def send_request(request_id, url, data, headers):
    response = requests.post(url, data=data, headers=headers)
    print('============Response for request ' + str(request_id) + '===================')
    print(response.text)

url = "http://teamtest-db-02.eng:58080/data-manager/1.1.0/services/traffic"
data = json.dumps(construct_ws_request())
headers = {'Content-Type': 'application/json', 'Accept': 'application/json'}
q = queue.Queue()
req_id=0
while True:
    req_id = req_id + 1
    t = threading.Thread(target=send_request, args = (req_id, url, data, headers))
    t.daemon = True
    t.start()
    time.sleep(1)

