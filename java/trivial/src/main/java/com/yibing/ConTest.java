package com.yibing;

import org.joda.time.DateTime;

import java.io.*;
import java.util.*;

public class ConTest
{
    public static void main(String args[])
    {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(
                1, 3, 5, 7, 9, 2, 4, 6, 8, 10
        ));
        Collections.sort(list, Collections.reverseOrder());
        System.out.println(list);

//        Console cons = System.console();
//        String username = cons.readLine("User name: ");
//        char[] passwd = cons.readPassword("Password: ");

//        System.out.println("Hi " + username + ", your password is: " + new String(passwd));
        
        System.out.printf("now is: %tc", new Date());

        long amount = 10000;
        System.out.println(String.format("amount = %s", amount));

        double d = 11112222333344445555d;
        long l = (long) d;
        System.out.println("double = " + d + ", long = " + l);

        String s = "\u00AC";
        System.out.println(s);

        System.out.println("dataset:hdfs:///".replaceAll("^\\.:", ""));


        DateTime dt = new DateTime("2016-01-01T13:00:01.123456789+01:00");
        System.out.println(dt);


    }
}