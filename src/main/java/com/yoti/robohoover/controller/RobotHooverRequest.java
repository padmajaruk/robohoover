package com.yoti.robohoover.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RobotHooverRequest {
    @JsonProperty("roomSize")
    @NotNull
    private List<Integer> roomSize;
    @JsonProperty("coords")
    @NotNull
    private List<Integer> hooverCoordinates;
    @JsonProperty("patches")
    private List<List<Integer>> patches;
    @JsonProperty("instructions")
    private String instructions;

    public List<Integer> getRoomSize() {
        return roomSize;
    }

    public List<Integer> getHooverCoordinates() {
        return hooverCoordinates;
    }

    public List<List<Integer>> getPatches() {
        return patches;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setRoomSize(List<Integer> roomSize) {
        this.roomSize = roomSize;
    }

    public void setHooverCoordinates(List<Integer> hooverCoordinates) {
        this.hooverCoordinates = hooverCoordinates;
    }

    public void setPatches(List<List<Integer>> patches) {
        this.patches = patches;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
