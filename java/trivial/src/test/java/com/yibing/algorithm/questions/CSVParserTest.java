package com.yibing.algorithm.questions;

import com.yibing.algorithm.questions.CSVParser;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class CSVParserTest {
  private CSVParser parser;

  @BeforeClass
  public void setup() {
    this.parser = new CSVParser();
  }

  @DataProvider(name = "cases")
  public Object[][]testCases() {
    return new Object[][] {
        {
            "column1,column2,column3\n" +
                "aaa,a\\,b,\n" +
                ",\"bb,cc\",cc\n" +
                ",,\n" +
                ",a\"a,b\"b,\n",
            Arrays.asList(
                Arrays.asList("column1", "column2", "column3"),
                Arrays.asList("aaa", "a,b", ""),
                Arrays.asList("", "bb,cc", "cc"),
                Arrays.asList("", "", ""),
                Arrays.asList("", "aa,bb", "")
            )
        },
    };
  }

  @Test(dataProvider = "cases")
  public void testParser(String input, List<List<String>> expectedResult) throws IOException {
    List<List<String>> result = this.parser.parse(new BufferedReader(new StringReader(input)));
    assertEquals(result, expectedResult);
  }
}
