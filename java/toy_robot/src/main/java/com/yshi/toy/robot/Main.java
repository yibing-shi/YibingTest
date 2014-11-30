package com.yshi.toy.robot;

import com.yshi.toy.robot.control.ControlException;
import com.yshi.toy.robot.control.command.Command;
import com.yshi.toy.robot.input.CommandReader;
import com.yshi.toy.robot.input.FileCommandReader;
import com.yshi.toy.robot.control.command.parse.CommandParser;
import com.yshi.toy.robot.control.command.parse.TextCommandParser;
import com.yshi.toy.robot.control.RobotController;
import com.yshi.toy.robot.entity.Robot;
import com.yshi.toy.robot.entity.Tabletop;

import java.io.FileNotFoundException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.err.println("Need 1 parameter: <command file path>");
            return;
        }

        final CommandParser parser = new TextCommandParser();
        final CommandReader commandReader =  new FileCommandReader(parser, args[0]);
        final Tabletop tabletop = new Tabletop(5, 5);
        final Robot robot = new Robot("Robot1");
        final RobotController controller = new RobotController(robot, tabletop);
        final Iterator<Command> commandIterator = commandReader.getCommandIterator();
        while (commandIterator.hasNext()) {
            final Command command = commandIterator.next();
            try {
                command.execute(controller);
            } catch (ControlException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
