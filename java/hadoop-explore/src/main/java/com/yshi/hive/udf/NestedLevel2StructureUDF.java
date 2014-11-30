package com.yshi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NestedLevel2StructureUDF extends GenericUDF {
    public static class Lv2Struct {
        public String f2_2_1;
        public String f2_2_2;
    }

    public static class Lv1Struct {
        public String f2_1;
        public Lv2Struct f2_2;
    }

    public static class RootStruct {
        public String f1;
        public Lv1Struct f2;
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] objectInspectors) throws UDFArgumentException {
        final StructObjectInspector lv2StructOI = ObjectInspectorFactory.getStandardStructObjectInspector(
                Arrays.asList("f2_2_1", "f2_2_2"),
                Arrays.asList((ObjectInspector)PrimitiveObjectInspectorFactory.javaStringObjectInspector,
                        (ObjectInspector)PrimitiveObjectInspectorFactory.javaStringObjectInspector)
        );
        final StructObjectInspector lv1StructOI = ObjectInspectorFactory.getStandardStructObjectInspector(
                Arrays.asList("f2_1", "f2_2"),
                Arrays.asList((ObjectInspector)PrimitiveObjectInspectorFactory.javaStringObjectInspector,
                        (ObjectInspector)lv2StructOI)
        );
        final StructObjectInspector rootStructOI = ObjectInspectorFactory.getStandardStructObjectInspector(
                Arrays.asList("f1", "f2"),
                Arrays.asList((ObjectInspector)PrimitiveObjectInspectorFactory.javaStringObjectInspector,
                        (ObjectInspector)lv1StructOI)
        );
        return rootStructOI;
    }

    @Override
    public Object evaluate(DeferredObject[] deferredObjects) throws HiveException {
        List<Object> lv2StructureData = new ArrayList<Object>();
        lv2StructureData.add("f2_2_1");
        lv2StructureData.add("f2_2_2");

        List<Object> lv1StructureData = new ArrayList<Object>();
        lv1StructureData.add("f2_1");
        lv1StructureData.add(lv2StructureData);

        List<Object> rootStructureData = new ArrayList<Object>();
        rootStructureData.add("f1");
        rootStructureData.add(lv1StructureData);

        return rootStructureData;
    }

    @Override
    public String getDisplayString(String[] strings) {
        return "NestedLevel2StructureUDF";
    }
}
