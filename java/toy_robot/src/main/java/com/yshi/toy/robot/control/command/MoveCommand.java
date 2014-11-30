package com.yshi.toy.robot.control.command;

import com.yshi.toy.robot.control.Facing;
import com.yshi.toy.robot.control.Position;
import com.yshi.toy.robot.control.RobotController;

public class MoveCommand implements Command{

    @Override
    public void execute(RobotController robotController) {
        robotController.move();
    }

}
