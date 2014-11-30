package com.yshi.toy.robot.control.command;

import com.yshi.toy.robot.MockTest;
import com.yshi.toy.robot.control.RobotController;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ReportCommandTest extends MockTest {

    @Mock private RobotController robotController;

    @Test
    public void testExecute() {
        final String POSITION_INFO = "1,2,NORTH";
        Mockito.when(robotController.report()).thenReturn(POSITION_INFO);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        ReportCommand command = new ReportCommand();
        command.setPrintStream(printStream);
        command.execute(robotController);
        assertEquals(byteArrayOutputStream.toString(), POSITION_INFO + System.getProperty("line.separator"));
    }
}
