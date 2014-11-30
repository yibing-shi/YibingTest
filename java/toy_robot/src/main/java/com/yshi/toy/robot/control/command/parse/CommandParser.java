package com.yshi.toy.robot.control.command.parse;

import com.yshi.toy.robot.control.command.Command;

public interface CommandParser {
    Command parse(String input) throws ParseException;
}
