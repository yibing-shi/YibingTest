package com.yshi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class NumberUDF extends UDF {

    public LongWritable evaluate(Text input) {
        return new LongWritable(Long.valueOf(input.toString()));
    }

}
