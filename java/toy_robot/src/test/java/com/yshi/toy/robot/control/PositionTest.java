package com.yshi.toy.robot.control;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class PositionTest {

    @Test
    public void testEquals() {
        assertEquals(new Position(1, 2), new Position(1, 2));
    }

    @Test
    public void testHashCode() {
        assertEquals(new Position(1, 2).hashCode(), new Position(1, 2).hashCode());
    }
}
