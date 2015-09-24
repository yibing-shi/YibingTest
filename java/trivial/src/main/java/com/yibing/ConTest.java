package com.yibing;

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
    }
}