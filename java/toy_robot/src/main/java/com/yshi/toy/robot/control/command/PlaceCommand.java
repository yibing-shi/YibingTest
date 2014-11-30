package com.yshi.toy.robot.control.command;

import com.yshi.toy.robot.control.Facing;
import com.yshi.toy.robot.control.Position;
import com.yshi.toy.robot.control.RobotController;

public class PlaceCommand implements Command{
    private final Position position;
    private final Facing facing;

    public PlaceCommand(Position position, Facing facing) {
        this.position = position;
        this.facing = facing;
    }

    public Position getPosition() {
        return position;
    }

    public Facing getFacing() {
        return facing;
    }

    @Override
    public void execute(RobotController robotController) {
        robotController.place(this.position, this.facing);
    }

}
