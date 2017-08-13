package com.yoti.robohoover.service;

import com.yoti.robohoover.service.Position;

public class RobotHooverServiceResponse {
    private Position position;
    private int patchesCount;

    public RobotHooverServiceResponse(Position hooverPosition, int patchesCount) {
        this.position = hooverPosition;
        this.patchesCount = patchesCount;
    }

    public Position getPosition() {
        return position;
    }
    public int getPatchesCount() {
        return patchesCount;
    }
}
