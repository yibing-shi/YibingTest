package com.yibing;

import java.io.*;
import java.util.Random;

public class Converter {
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Parameters: <input file> <output file> <increase percentage>");
            return;
        }

        final BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        final BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
        final int percentage = Integer.valueOf(args[2]);

        Random randomGenerator = new Random(System.nanoTime());
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.newLine();

            if (randomGenerator.nextInt(100) > percentage)
                continue;

            String[] fields = line.split(",");
            int originalPartnerId = Integer.valueOf(fields[3]);
            fields[3] = String.valueOf(originalPartnerId + 10);
            for (int i = 6; i < 30; i++) {
                long originalVal = Long.valueOf(fields[i]);
                fields[i] = String.valueOf(originalVal * percentage / 100);
            }

            writer.write(fields[0]);
            for (int i = 1; i < fields.length; ++i) {
                writer.write(",");
                writer.write(fields[i]);
            }
            writer.newLine();
        }

        reader.close();
        writer.close();
    }
}
