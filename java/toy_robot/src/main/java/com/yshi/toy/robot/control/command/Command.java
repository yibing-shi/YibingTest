package com.yshi.toy.robot.control.command;

import com.yshi.toy.robot.control.RobotController;

public interface Command {
    void execute(RobotController robotController);
}
