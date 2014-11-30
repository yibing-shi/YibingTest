package com.yshi.toy.robot.input;

import com.yshi.toy.robot.control.command.Command;
import com.yshi.toy.robot.control.command.parse.ParseException;
import com.yshi.toy.robot.control.command.parse.CommandParser;

import java.util.Iterator;

public abstract class BasicCommandReader implements CommandReader {

    private CommandParser parser;

    protected BasicCommandReader(CommandParser parser) {
        this.parser = parser;
    }

    private class CommandIterator implements Iterator<Command> {
        private Command currentCommand = null;

        @Override
        public boolean hasNext() {
            final String currentInput = readNext();
            if (currentInput == null) {
                return false;
            }

            try {
                currentCommand = parser.parse(currentInput);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            return true;
        }

        @Override
        public Command next() {
            return currentCommand;
        }

        @Override
        public void remove() {
            throw new RuntimeException("Operation remove is not supported!");
        }
    }

    @Override
    public Iterator<Command> getCommandIterator() {
        return new CommandIterator();
    }

    abstract protected String readNext();
}
