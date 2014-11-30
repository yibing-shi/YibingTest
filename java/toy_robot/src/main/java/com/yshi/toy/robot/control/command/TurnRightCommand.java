package com.yshi.toy.robot.control.command;

import com.yshi.toy.robot.control.RobotController;

public class TurnRightCommand implements Command{

    @Override
    public void execute(RobotController robotController) {
        robotController.turnRight();
    }

}
