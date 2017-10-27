package com.yshi.hive.udf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.MapredContext;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@UDFType(deterministic = false)
public class GenericLookupUDF extends GenericUDF {

  protected transient Log LOG = LogFactory.getLog(this.getClass());

  private Map<Integer, String> lookupMap = null;
  private JobConf jobConf = null;
  private IntObjectInspector ioi;
  private StringObjectInspector soi;

  @Override
  public void configure(MapredContext context) {
    super.configure(context);
    jobConf = context.getJobConf();
    LOG.info("Get job conf: " + jobConf);
  }

  @Override
  public ObjectInspector initialize(ObjectInspector[] objectInspectors) throws UDFArgumentException {
    if (objectInspectors.length != 2) {
      throw new UDFArgumentException("need 2 parameters");
    }

    if (! (objectInspectors[0] instanceof IntObjectInspector)) {
      throw new UDFArgumentException("first parameter must be a int. The one provided is: " + objectInspectors[0]);
    }
    ioi = (IntObjectInspector) objectInspectors[0];

    if (!(objectInspectors[1] instanceof StringObjectInspector)) {
      throw new UDFArgumentException("second parameter must be a string. The one provided is:" + objectInspectors[1]);
    }
    soi = (StringObjectInspector) objectInspectors[1];

    return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
  }

  @Override
  public Object evaluate(DeferredObject[] deferredObjects) throws HiveException {
    Integer key = ioi.get(deferredObjects[0].get());
    String filePath = soi.getPrimitiveJavaObject(deferredObjects[1].get());

    if (lookupMap == null) {
      constructLookupMap(filePath);
    }

    String result = lookupMap.get(key);
    if (result == null) {
      LOG.warn("Result for [" + key + "] is null. ");
      return null;
    }
    return new Text(result);
  }

  private void constructLookupMap(String filePath) throws HiveException {
    lookupMap = new HashMap<Integer, String>();
    try {
      BufferedReader bufferedReader = new BufferedReader(getLookupFileReader(filePath));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        LOG.debug("Lookup file content:" + line);
        String[] parts = line.split("=>");
        if (parts.length != 2) {
          LOG.warn("Invalid lookup data: " + line);
          continue;
        }
        LOG.debug("Add entry to lookup map: " + Arrays.deepToString(parts));
        lookupMap.put(Integer.valueOf(parts[0]), parts[1]);
      }
    } catch (IOException e) {
      LOG.error("Cannot read lookup file " + filePath, e);
      throw new HiveException("Cannot read lookup file " + filePath + ":" + e.getMessage(), e);
    }
    LOG.info("Successfully construct lookup map");
  }

  private Reader getLookupFileReader(final String filePath) throws IOException {
    if (filePath.startsWith("hdfs://")) {
      FileSystem fs = FileSystem.get(jobConf);
      return new InputStreamReader(fs.open(new Path(filePath)));
    } else {
      return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath));
    }
  }

  @Override
  public String getDisplayString(String[] strings) {
    return "Generic UDF function to lookup values in a lookup file\n" +
        "parameter 1: key\n" +
        "parameter 2: filename";
  }
}
