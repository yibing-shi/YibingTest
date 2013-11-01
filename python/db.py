import pymysql
import json

conn = pymysql.connect(host='localhost', port=3306, user='root', passwd='', db='em_topline')
cur = conn.cursor()

try:
    cur.execute('SELECT DISTINCT entity_id FROM em_topline.publication_choice_day')
    entity_ids = []
    for i in cur.fetchall():
        entity_ids.append(i[0])
    print("entity ids: " + str(entity_ids))

    for entity_id in entity_ids:
        print('Processing entity ' + str(entity_id))
        cur.execute('''
                SELECT entity_id, category_id, country_code, partner_id, first_party, time_index, demo_id, value 
                FROM em_topline.publication_choice_day 
                WHERE entity_id=''' + str(entity_id))

        result_map = {}
        for r in cur.fetchall():
            key = r[:6]
            choice_id = r[6]
            choice_value = r[7]
            if key in result_map:
                choice_value_map = result_map[key]
            else:
                choice_value_map = {}
            choice_value_map[choice_id] = choice_value
            result_map[key] = choice_value_map

        for record_key, record_value in result_map.items():
            text = json.dumps(record_value)
            sql = "REPLACE INTO em_topline.publication_choice_day_ng VALUES ('" + "', '".join(map(str, record_key)) + "', '" + json.dumps(record_value) + "')"
            print(sql)
            cur.execute(sql)
finally:
    try:
        cur.close()
    finally:
        conn.close()

