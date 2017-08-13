package com.yoti.robohoover.service;

import com.yoti.robohoover.exception.InvalidInputException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.yoti.robohoover.enumeration.Instruction.*;
import static java.util.Objects.nonNull;

@Service("roboHooverService")
public class RobotHooverServiceImpl implements RobotHooverService {
    private static final String VALID_INSTRUCTIONS_REGEX = "[NWES]*";
    private int patchesCount = 0;
    private List<Position> cleanedPatches;

    public RobotHooverServiceResponse runHoover(RobotHooverServiceRequest robotHooverRequest) {
        initialiseCounters();
        applyInstructions(robotHooverRequest);
        RobotHooverServiceResponse robotHooverServiceResponse =
                new RobotHooverServiceResponse(robotHooverRequest.getHooverPosition(), patchesCount);
        cleanCounters();
        return robotHooverServiceResponse;
    }

    private void applyInstructions(RobotHooverServiceRequest robotHooverRequest) {
        String instructions = robotHooverRequest.getInstructions();
        if (isInstructionExists(instructions)) {
            validateInstructions(instructions.toUpperCase());
            instructions.toUpperCase().chars()
                    .forEach(instruction -> moveRoboHoover(robotHooverRequest, (char) instruction));
        }
    }

    private void moveRoboHoover(RobotHooverServiceRequest robotHooverServiceRequest, char instruction) {
        Position hooverPosition = robotHooverServiceRequest.getHooverPosition();
        List<Position> patches = robotHooverServiceRequest.getPatches();
        moveDirection(instruction, robotHooverServiceRequest.getRoomPosition(), hooverPosition);
        checkCoordinatesWithPatches(patches, hooverPosition);
    }

    private void moveDirection(char instruction, Position roomPosition, Position hooverPosition) {
        if (isNorth(instruction)) {
            moveNorth(roomPosition, hooverPosition);
        } else if (isEast(instruction)) {
            moveEast(roomPosition, hooverPosition);
        } else if (isWest(instruction)) {
            moveWest(roomPosition, hooverPosition);
        } else {
            moveSouth(roomPosition, hooverPosition);
        }
    }

    private void moveWest(Position roomPosition, Position hooverPosition) {
        if (roomPosition.getX() > 0) {
            hooverPosition.setX(hooverPosition.getX() - 1);
        }
    }

    private void moveEast(Position roomPosition, Position hooverPosition) {
        if (roomPosition.getX() > hooverPosition.getX()) {
            hooverPosition.setX(hooverPosition.getX() + 1);
        }
    }

    private void moveSouth(Position roomPosition, Position hooverPosition) {
        if (roomPosition.getY() > 0) {
            hooverPosition.setY(hooverPosition.getY() - 1);
        }
    }

    private void moveNorth(Position roomPosition, Position hooverPosition) {
        if (roomPosition.getY() > hooverPosition.getY()) {
            hooverPosition.setY(hooverPosition.getY() + 1);
        }
    }

    private void validateInstructions(String instructions) {
        boolean matches = instructions.matches(VALID_INSTRUCTIONS_REGEX);
        if (!matches) {
            throw new InvalidInputException("Invalid instructions");
        }
    }

    private boolean isInstructionExists(String instructions) {
        return nonNull(instructions) && instructions.length() > 0;
    }

    private void checkCoordinatesWithPatches(List<Position> patches, Position hooverPosition) {
        if (patches.contains(hooverPosition) && !isCleanedPatch(hooverPosition)) {
            cleanedPatches.add(hooverPosition);
            patchesCount++;
        }
    }

    private boolean isWest(char charAt) {
        return WEST == getInstruction(charAt);
    }

    private boolean isEast(char charAt) {
        return EAST == getInstruction(charAt);
    }

    private boolean isNorth(char charAt) {
        return NORTH == getInstruction(charAt);
    }

    private boolean isCleanedPatch(Position hooverPosition) {
        return cleanedPatches.contains(hooverPosition);
    }

    private void initialiseCounters() {
        patchesCount = 0;
        cleanedPatches = new ArrayList<>();
    }

    private void cleanCounters() {
        patchesCount = 0;
        cleanedPatches = null;
    }
}


