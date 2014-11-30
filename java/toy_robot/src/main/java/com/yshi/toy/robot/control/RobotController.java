package com.yshi.toy.robot.control;

import com.yshi.toy.robot.entity.Robot;
import com.yshi.toy.robot.entity.Tabletop;

public class RobotController {
    private final Robot robot;
    private final Tabletop tabletop;
    private Position position;
    private Facing facing;

    public RobotController(Robot robot, Tabletop tabletop) {
        this.robot = robot;
        this.tabletop = tabletop;
        this.position = null;
        this.facing = null;
    }

    public Robot getRobot() {
        return robot;
    }

    public Tabletop getTabletop() {
        return tabletop;
    }

    public Position getPosition() {
        return position;
    }

    public Facing getFacing() {
        return facing;
    }

    public void place(Position position, Facing facing) {
        if (tabletop == null) {
            return;
        }

        if (!isValidPosition(position)) {
            throw new ControlException("Invalid position: " + position);
        }

        this.position = position;
        this.facing = facing;
    }

    public void move() {
        if (tabletop == null) {
            return;
        }

        final Position newPos = getNextPos();
        if (!isValidPosition(newPos)) {
            throw new ControlException("This move command will cause robot fall over table top, ignore!");
        }

        this.position = newPos;
    }

    public void turnLeft() {
        if (this.facing == null) {
            throw new ControlException("Initial facing direction hasn't been set. Cannot turn left");
        }

        switch (this.facing) {
            case NORTH:
                this.facing = Facing.WEST;
                break;
            case WEST:
                this.facing = Facing.SOUTH;
                break;
            case SOUTH:
                this.facing = Facing.EAST;
                break;
            case EAST:
                this.facing = Facing.NORTH;
                break;
            default:
                throw new RuntimeException("Unknown Facing: " + this);
        }
    }

    public void turnRight() {
        if (this.facing == null) {
            throw new ControlException("Initial facing direction hasn't been set. Cannot turn right");
        }

        switch (this.facing) {
            case NORTH:
                this.facing =  Facing.EAST;
                break;
            case EAST:
                this.facing =  Facing.SOUTH;
                break;
            case SOUTH:
                this.facing =  Facing.WEST;
                break;
            case WEST:
                this.facing =  Facing.NORTH;
                break;
            default:
                throw new RuntimeException("Unknown Facing: " + this);
        }
    }

    public String report() {
        return "" + position.getX() + ',' + position.getY() + ',' + facing;
    }

    Position getNextPos() {
        Position newPos;
        switch (facing) {
            case NORTH:
                newPos = new Position(position.getX(), position.getY() + 1);
                break;
            case SOUTH:
                newPos = new Position(position.getX(), position.getY() - 1);
                break;
            case EAST:
                newPos = new Position(position.getX() + 1, position.getY());
                break;
            case WEST:
                newPos = new Position(position.getX() - 1, position.getY());
                break;
            default:
                throw new RuntimeException("Unknown Facing: " + facing);
        }

        return newPos;
    }

    boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < tabletop.getWidth()
                && position.getY() >= 0 && position.getY() < tabletop.getHeight();
    }

}
