package com.yshi.toy.robot.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RobotTest {

    @Test
    public void testGetId() {
        Robot robot = new Robot("r");
        assertNotNull(robot.getId());
    }

    @Test
    public void testNoDuplicateId() {
        Robot robot1 = new Robot("r1");
        Robot robot2 = new Robot("r1");
        assertNotEquals(robot1.getId(), robot2.getId());
    }

    @Test
    public void testGetName() {
        final String name = "robotName";
        Robot robot = new Robot(name);
        assertEquals(robot.getName(), name);
    }
}
