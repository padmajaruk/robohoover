package com.yoti.robohoover.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RobotHooverResponse {
    @JsonProperty("coords")
    private List<Integer> hooverCoordinates;
    @JsonProperty("patches")
    private Integer count;

    public List<Integer> getHooverCoordinates() {
        return hooverCoordinates;
    }

    public void setHooverCoordinates(List<Integer> hooverCoordinates) {
        this.hooverCoordinates = hooverCoordinates;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
