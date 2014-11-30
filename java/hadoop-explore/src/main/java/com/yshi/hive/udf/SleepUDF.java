package com.yshi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class SleepUDF extends UDF {

    public Text evaluate(Text input, int seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Text("DummyUDF--" + input.toString());
    }

}
