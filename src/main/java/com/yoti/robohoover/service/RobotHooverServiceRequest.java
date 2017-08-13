package com.yoti.robohoover.service;

import com.yoti.robohoover.service.Position;

import java.util.List;

public class RobotHooverServiceRequest {
    private Position roomPosition;
    private Position hooverPosition;
    private List<Position> patches;
    private String instructions;

    public RobotHooverServiceRequest(Position roomPosition, Position hooverPosition, List<Position> patches, String instructions) {
        this.roomPosition = roomPosition;
        this.hooverPosition = hooverPosition;
        this.patches = patches;
        this.instructions = instructions;
    }

    public Position getRoomPosition() {
        return roomPosition;
    }

    public Position getHooverPosition() {
        return hooverPosition;
    }

    public List<Position> getPatches() {
        return patches;
    }

    public String getInstructions() {
        return instructions;
    }
}
