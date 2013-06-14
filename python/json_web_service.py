#!/usr/bin/python

import json
import requests
import queue
import threading
import time

def create_traffic_insert_request():
    item1 = {}
    item1['entity_id'] = 0
    item1['category_id'] = 0
    item1['region_id'] = 1
    item1['country_code'] = 36
    item1['area_id'] = 0
    item1['metric'] = 1
    item1['time_index'] = 2113010200
    item1['partner_id'] = 1
    item1['desktop_value'] = 11
    item1['mobile_value'] = 12

    item2 = dict(item1)
    item2['metric'] = 2
    item2['desktop_value'] = 21
    item2['mobile_value'] = 22

    item3 = dict(item1)
    item3['metric'] = 3
    item3['desktop_value'] = 31
    item3['mobile_value'] = 32

    item4 = dict(item1)
    item4['metric'] = 4
    item4['desktop_value'] = 41
    item4['mobile_value'] = 42

    item5 = dict(item1)
    item5['metric'] = 5
    item5['desktop_value'] = 51
    item5['mobile_value'] = 52

    data_list = [item1, item2, item3, item4, item5]
    body = {}
    body['timeGroupCode'] = 1
    body['entityTypeCode'] = 'publication'
    body['deviceMobile'] = False
    body['trafficDataList'] = data_list
    return body

def create_people_count_insert_request():
    body = {}
    body['entityId'] = 0
    body['entityType'] = None
    body['countryCode'] = 99
    body['timeIndex'] = 2113010100
    body['timeGroup'] = 'MONTH'
    body['partnerId'] = 1
    body['peopleCountId'] = 2
    body['desktopDevices'] = 10.999
    body['mobileDevices'] = 99
    return body

def create_factor_insert_request():
    body = {}
    body['entityId'] = 0
    body['entityType'] = 'publication'
    body['partnerId'] = 1
    body['countryCode'] = 36
    body['timeIndex'] = 2113010100
    body['timeGroup'] = 1
    body['metric'] = 2
    body['desktopFactor'] = 1.02
    body['desktopLastFactor'] = 1.02
    body['mobileFactor'] = 1.02
    body['mobileLastFactor'] = 1.02
    return body

def create_populate_last_request():
    body = {}
    body['entityTypeCode'] = 'publication'
    body['timeGroupCode'] = '1'
    body['regionId'] = '1'
    body['timeIndex'] = '2113010200'
    body['lastTimeIndex'] = '2113010100'
    body['panelData'] = True
    body['uniqueBrowserAndUniqueVisitor'] = True
    body['panelType'] = 'partner_panel'
    return body

def create_traffic_aggr_request():
    body = {}
    body['entityType'] = 'publication'
    body['regionId'] = 1
    body['timeIndex'] = '2113010100'
    body['timeGroup'] = 'MONTH'
    body['panel'] = True
    return body

def create_traffic_request():
    body = {}
    body['entityId'] = '17787'
    body['categoryId'] = '0'
    body['partnerId'] = '-1'
    body['regionId'] = '1'
    body['countryCode'] = '36'
    body['startDateTime'] = '2013010100'
    body['endDateTime'] = '2013010200'
    body['timeGroupCode'] = '1'
    body['entityTypeCode'] = 'publication'
    body['metric'] = '1,2,3,4,5,6'
    body['avg'] = True
    body['orderBy'] = True
    return body

def create_traffic_combined_load_req():
    load_request = create_traffic_request()
    body = {'trafficRequests' : [load_request, load_request]}
    return body

def create_establishment_survey_req():
    body = {'countryCodes' : ['36', '360']}
    return body

def send_request(url, request_type, request_body):
    request = {}
    request['type'] = request_type
    request['body'] = request_body

    data = json.dumps(request, sort_keys=True, indent=4, separators=(',', ': '))
    headers = {'Content-Type': 'application/json', 'Accept': 'application/json'}
    response = requests.post(url, data=data, headers=headers)
    print(data)
    print(response.text)

if __name__=="__main__":
    #endpoint = "http://yshi-vm:38080/data-manager/1.1.0/services/"
    #endpoint = "http://localhost:8080/data-manager/1.1.0/services/"
    endpoint = "http://management1.site1:58082/data-manager/1.1.0/services/"
    #endpoint = "http://app1.site1:48071/data-manager/1.1.0/services/"
    #send_request(endpoint + "traffic", 'TRAFFIC_INSERT_REQUEST', create_traffic_insert_request())
    #send_request(endpoint + 'traffic', 'TRAFFIC_POPULATE_LAST_REQUEST', create_populate_last_request())
    #send_request(endpoint + 'traffic', 'TRAFFIC_AGGREGATION_REQUEST', create_traffic_aggr_request())
    send_request(endpoint + "traffic", 'TRAFFIC_REQUEST', create_traffic_request())
    #send_request(endpoint + "traffic", 'TRAFFIC_COMBINED_LOAD_REQUEST', create_traffic_combined_load_req())
    #send_request(endpoint + "factor", 'FACTOR_INFO_INSERT_REQUEST', create_factor_insert_request())
    #send_request(endpoint + "people", 'PEOPLE_COUNT_INSERT_REQUEST', create_people_count_insert_request())
    #send_request(endpoint + 'establishment-survey', 'ESTABLISHMENT_SURVEY_REQUEST', create_establishment_survey_req())
