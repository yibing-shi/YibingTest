package com.yshi.toy.robot.input;

import com.yshi.toy.robot.control.command.parse.CommandParser;

import java.io.*;

public class FileCommandReader extends BasicCommandReader {

    private final BufferedReader reader;

    public FileCommandReader(CommandParser parser, String filePath) throws FileNotFoundException {
        super(parser);
        reader = new BufferedReader(new FileReader(filePath));
    }

    @Override
    protected String readNext() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
