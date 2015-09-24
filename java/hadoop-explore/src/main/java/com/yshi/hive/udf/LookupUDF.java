package com.yshi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LookupUDF extends UDF {
    Map<Integer, String> lookupMap = null;

    public String evaluate(Integer key, String filePath){
        if (lookupMap == null) {
            lookupMap = new HashMap<Integer, String>();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split("=>");
                    if (parts.length != 2) {
                        System.err.println("Invalid lookup data: " + line);
                        continue;
                    }
                    lookupMap.put(Integer.valueOf(parts[0]), parts[1]);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (lookupMap.containsKey(key)) {
            return lookupMap.get(key);
        }

        return null;
    }

}
