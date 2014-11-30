package com.yshi.toy.robot.control.command.parse;

import com.yshi.toy.robot.control.command.*;
import com.yshi.toy.robot.control.Facing;
import com.yshi.toy.robot.control.Position;

import java.util.Arrays;

public class TextCommandParser implements CommandParser {
    private static final String PLACE_OPERATOR = "PLACE";
    private static final String MOVE_OPERATOR = "MOVE";
    private static final String TURN_LEFT_OPERATOR = "LEFT";
    private static final String TURN_RIGHT_OPERATOR = "RIGHT";
    private static final String REPORT_OPERATOR = "REPORT";

    @Override
    public Command parse(String input) throws ParseException {
        String[] parts = input.split(" ");
        String operator = parts[0].toUpperCase();

        if (operator.equals(PLACE_OPERATOR)) {
            return parsePlaceCommand(parts);
        } else if (operator.equals(MOVE_OPERATOR)) {
            return parseMoveCommand(parts);
        } else if (operator.equals(TURN_LEFT_OPERATOR)) {
            return parseTurnLeftCommand(parts);
        } else if (operator.equals(TURN_RIGHT_OPERATOR)) {
            return parseTurnRightCommand(parts);
        } else if (operator.equals(REPORT_OPERATOR)) {
            return parseReportCommand(parts);
        } else {
            throw new ParseException("Unknown command: " + input);
        }
    }

    private PlaceCommand parsePlaceCommand(String[] parts) throws ParseException {
        if (parts.length != 2) {
            throw new ParseException("PLACE command needs parameters");
        }
        String[] params = parts[1].split(",");
        if (params.length != 3) {
            throw new ParseException("PLACE command needs 3 parameter, but "
                    + params.length + " are provided");
        }

        try {
            int x = Integer.valueOf(params[0]);
            int y = Integer.valueOf(params[1]);
            Facing facing = Facing.valueOf(params[2]);
            return new PlaceCommand(new Position(x, y), facing);
        } catch (Exception e) {
            throw new ParseException("Invalid parameters in PLACE command: "
                    + Arrays.deepToString(params), e);
        }
    }

    private MoveCommand parseMoveCommand(String[] parts) throws ParseException {
        if (parts.length != 1) {
            throw new ParseException("MOVE command takes no parameter");
        }
        return new MoveCommand();
    }

    private TurnLeftCommand parseTurnLeftCommand(String[] parts) throws ParseException {
        if (parts.length != 1) {
            throw new ParseException("LEFT command takes no parameter");
        }
        return new TurnLeftCommand();
    }

    private TurnRightCommand parseTurnRightCommand(String[] parts) throws ParseException {
        if (parts.length != 1) {
            throw new ParseException("RIGHT command takes no parameter");
        }
        return new TurnRightCommand();
    }

    private ReportCommand parseReportCommand(String[] parts) throws ParseException {
        if (parts.length != 1) {
            throw new ParseException("REPORT command takes no parameter");
        }
        return new ReportCommand();
    }

}
