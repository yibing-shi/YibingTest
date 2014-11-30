package com.yshi.toy.robot.control.command;

import com.yshi.toy.robot.control.RobotController;

import java.io.PrintStream;

public class ReportCommand implements Command{

    private PrintStream printStream = System.out;

    @Override
    public void execute(RobotController robotController) {
        printStream.println(robotController.report());
    }

    void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }

}
