package com.yshi.toy.robot.control;

import com.yshi.toy.robot.entity.Robot;
import com.yshi.toy.robot.entity.Tabletop;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RobotControllerTest {
    private RobotController controller;

    @BeforeMethod
    public void initController() {
        controller = new RobotController(new Robot("DummyRobot"), new Tabletop(5, 5));
    }

    @Test
    public void testPlace() {
        controller.place(new Position(1, 1), Facing.EAST);
        assertEquals(controller.getPosition(), new Position(1, 1));
        assertEquals(controller.getFacing(), Facing.EAST);

        controller.place(new Position(4, 4), Facing.NORTH);
        assertEquals(controller.getPosition(), new Position(4, 4));
        assertEquals(controller.getFacing(), Facing.NORTH);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testPlaceToInvalidPositionXTooSmall() {
        controller.place(new Position(-1, 1), Facing.EAST);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testPlaceToInvalidPositionXTooLarge() {
        controller.place(new Position(5, 1), Facing.EAST);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testPlaceToInvalidPositionYTooSmall() {
        controller.place(new Position(1, -1), Facing.EAST);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testPlaceToInvalidPositionYTooLarge() {
        controller.place(new Position(1, 5), Facing.EAST);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testPlaceToInvalidPositionXAndYTooLarge() {
        controller.place(new Position(5, 5), Facing.EAST);
    }

    @Test
    public void testMoveNorth() {
        controller.place(new Position(1,2), Facing.NORTH);
        controller.move();
        assertEquals(controller.getPosition(), new Position(1, 3));
        assertEquals(controller.getFacing(), Facing.NORTH);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testMoveNorthFalling() {
        controller.place(new Position(1, 4), Facing.NORTH);
        controller.move();
    }

    @Test
    public void testMoveWest() {
        controller.place(new Position(1,2), Facing.WEST);
        controller.move();
        assertEquals(controller.getPosition(), new Position(0, 2));
        assertEquals(controller.getFacing(), Facing.WEST);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testMoveWestFalling() {
        controller.place(new Position(0,4), Facing.WEST);
        controller.move();
    }

    @Test
    public void testMoveSouth() {
        controller.place(new Position(1,2), Facing.SOUTH);
        controller.move();
        assertEquals(controller.getPosition(), new Position(1, 1));
        assertEquals(controller.getFacing(), Facing.SOUTH);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testMoveSouthFalling() {
        controller.place(new Position(1,0), Facing.SOUTH);
        controller.move();
    }

    @Test
    public void testMoveEast() {
        controller.place(new Position(1,2), Facing.EAST);
        controller.move();
        assertEquals(controller.getPosition(), new Position(2, 2));
        assertEquals(controller.getFacing(), Facing.EAST);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testMoveEastFalling() {
        controller.place(new Position(4,1), Facing.EAST);
        controller.move();
    }

    @Test
    public void testFacingNorthTurnLeft() {
        controller.place(new Position(1, 1), Facing.NORTH);
        controller.turnLeft();
        assertEquals(controller.getFacing(), Facing.WEST);
    }

    @Test
    public void testFacingWestTurnLeft() {
        controller.place(new Position(1, 1), Facing.WEST);
        controller.turnLeft();
        assertEquals(controller.getFacing(), Facing.SOUTH);
    }

    @Test
    public void testFacingSouthTurnLeft() {
        controller.place(new Position(1, 1), Facing.SOUTH);
        controller.turnLeft();
        assertEquals(controller.getFacing(), Facing.EAST);
    }

    @Test
    public void testFacingEastTurnLeft() {
        controller.place(new Position(1, 1), Facing.EAST);
        controller.turnLeft();
        assertEquals(controller.getFacing(), Facing.NORTH);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testTurnLeftWithoutOriginalSetting() {
        controller.turnLeft();
    }

    @Test
    public void testFacingNorthTurnRight() {
        controller.place(new Position(1, 1), Facing.NORTH);
        controller.turnRight();
        assertEquals(controller.getFacing(), Facing.EAST);
    }

    @Test
    public void testFacingWestTurnRight() {
        controller.place(new Position(1, 1), Facing.EAST);
        controller.turnRight();
        assertEquals(controller.getFacing(), Facing.SOUTH);
    }

    @Test
    public void testFacingSouthTurnRight() {
        controller.place(new Position(1, 1), Facing.SOUTH);
        controller.turnRight();
        assertEquals(controller.getFacing(), Facing.WEST);
    }

    @Test
    public void testFacingEastTurnRight() {
        controller.place(new Position(1, 1), Facing.WEST);
        controller.turnRight();
        assertEquals(controller.getFacing(), Facing.NORTH);
    }

    @Test (expectedExceptions = ControlException.class)
    public void testTurnRightWithoutOriginalSetting() {
        controller.turnRight();
    }

    @Test
    public void testReport() {
        controller.place(new Position(1, 2), Facing.NORTH);
        assertEquals(controller.report(), "1,2,NORTH");
    }
}
