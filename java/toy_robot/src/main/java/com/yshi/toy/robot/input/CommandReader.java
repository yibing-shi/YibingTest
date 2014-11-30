package com.yshi.toy.robot.input;

import com.yshi.toy.robot.control.command.Command;

import java.util.Iterator;

public interface CommandReader {
    Iterator<Command> getCommandIterator();
}
