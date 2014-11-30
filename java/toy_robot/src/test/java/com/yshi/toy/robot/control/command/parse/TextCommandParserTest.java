package com.yshi.toy.robot.control.command.parse;

import com.yshi.toy.robot.control.Facing;
import com.yshi.toy.robot.control.Position;
import com.yshi.toy.robot.control.command.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TextCommandParserTest {

    private TextCommandParser parser;

    @BeforeMethod
    public void setParser() {
        parser = new TextCommandParser();
    }

    @Test
    public void testParseValidPlaceCommand() throws ParseException {
        Command command = parser.parse("PLACE 1,0,SOUTH");
        assertEquals(command.getClass(), PlaceCommand.class);
        PlaceCommand placeCommand = (PlaceCommand) command;
        assertEquals(placeCommand.getPosition(), new Position(1, 0));
        assertEquals(placeCommand.getFacing(), Facing.SOUTH);
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseTooManyParamsPlaceCommand() throws ParseException {
        parser.parse("PLACE 1,0,SOUTH redundant");
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseNoParamPlaceCommand() throws ParseException {
        parser.parse("PLACE");
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseInvalidXValuePlaceCommand() throws ParseException {
        parser.parse("PLACE a,0,SOUTH");
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseInvalidYValuePlaceCommand() throws ParseException {
        parser.parse("PLACE 1,b,SOUTH");
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseInvalidFacingPlaceCommand() throws ParseException {
        parser.parse("PLACE 1,2,UNKNOWN");
    }

    @Test
    public void testParseValidMoveCommand() throws ParseException {
        Command command = parser.parse("move ");
        assertEquals(command.getClass(), MoveCommand.class);
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseTooManyParamsMoveCommand() throws ParseException {
        parser.parse("MOVE 1");
    }

    @Test
    public void testParseValidTurnLeftCommand() throws ParseException {
        Command command = parser.parse("Left ");
        assertEquals(command.getClass(), TurnLeftCommand.class);
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseTooManyParamsTurnLeftCommand() throws ParseException {
        parser.parse("Left 1");
    }

    @Test
    public void testParseValidTurnRightCommand() throws ParseException {
        Command command = parser.parse("right ");
        assertEquals(command.getClass(), TurnRightCommand.class);
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseTooManyParamsTurnRightCommand() throws ParseException {
        parser.parse("right 1");
    }

    @Test
    public void testParseValidReportCommand() throws ParseException {
        Command command = parser.parse("REPort ");
        assertEquals(command.getClass(), ReportCommand.class);
    }

    @Test (expectedExceptions = ParseException.class)
    public void testParseTooManyParamsReportCommand() throws ParseException {
        parser.parse("REPort 1");
    }

}
