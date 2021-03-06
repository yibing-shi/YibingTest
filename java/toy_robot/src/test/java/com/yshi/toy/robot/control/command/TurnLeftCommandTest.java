package com.yshi.toy.robot.control.command;

import com.yshi.toy.robot.MockTest;
import com.yshi.toy.robot.control.RobotController;
import org.mockito.Mock;
import org.testng.annotations.Test;

import static org.mockito.Mockito.doAnswer;
import static org.testng.Assert.assertTrue;

public class TurnLeftCommandTest extends MockTest {

    @Mock private RobotController robotController;

    @Test
    public void testExecute() {
        final SetStateAnswer<Boolean> setStateAnswer = new SetStateAnswer<Boolean>(Boolean.TRUE);
        doAnswer(setStateAnswer).when(robotController).turnLeft();
        TurnLeftCommand command = new TurnLeftCommand();
        command.execute(robotController);
        assertTrue(setStateAnswer.getActualValue());
    }
}
