package com.yshi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.io.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@UDFType(deterministic = false)
public class LookupUDF extends UDF {

  public Text evaluate(Text filePath, int key) {
    Map<Integer, String> lookupMap = new HashMap<Integer, String>();
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toString()));
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

    String toReturn = lookupMap.get(key);
    return toReturn == null ? null : new Text(toReturn);
  }

}
