package com.yshi.toy.robot.control.command;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import com.yshi.toy.robot.MockTest;
import com.yshi.toy.robot.control.Facing;
import com.yshi.toy.robot.control.Position;
import com.yshi.toy.robot.control.RobotController;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlaceCommandTest extends MockTest {

    @Mock private RobotController robotController;

    @Test
    public void testExecute() {
        final SetStateAnswer<Boolean> setStateAnswer = new SetStateAnswer<Boolean>(Boolean.TRUE);
        doAnswer(setStateAnswer).when(robotController).place(any(Position.class), any(Facing.class));
        PlaceCommand command = new PlaceCommand(new Position(0, 0), Facing.EAST);
        command.execute(robotController);
        assertTrue(setStateAnswer.getActualValue());
    }
}
