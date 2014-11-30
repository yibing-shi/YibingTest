package com.yshi.toy.robot.control.command.parse;

public class ParseException extends Exception {
    public ParseException(final String reason) {
        this(reason, null);
    }

    public ParseException(final String reason, Throwable throwable) {
        super(reason, throwable);
    }
}
