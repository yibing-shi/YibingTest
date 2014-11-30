package com.yshi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class DummyUDF extends UDF {

    public Text evaluate(Text input) {
        return new Text("DummyUDF--" + input.toString());
    }

}
