package com.yshi.toy.robot.control.command;

import com.yshi.toy.robot.control.RobotController;

public class TurnLeftCommand implements Command{

    @Override
    public void execute(RobotController robotController) {
        robotController.turnLeft();
    }

}
