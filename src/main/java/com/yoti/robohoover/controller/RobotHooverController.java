package com.yoti.robohoover.controller;

import com.yoti.robohoover.exception.InvalidInputException;
import com.yoti.robohoover.service.Position;
import com.yoti.robohoover.service.RobotHooverServiceImpl;
import com.yoti.robohoover.service.RobotHooverServiceRequest;
import com.yoti.robohoover.service.RobotHooverServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RobotHooverController {

    @Autowired
    @Qualifier("roboHooverService")
    private RobotHooverServiceImpl robotHooverService;

    @RequestMapping(path = "/robohoover/process", method = POST)
    public
    @ResponseBody
    RobotHooverResponse process(@RequestBody RobotHooverRequest robotHooverRequest) {
        validateRequest(robotHooverRequest);
        RobotHooverServiceRequest roboHooverRequest = mapToRequest(robotHooverRequest);
        RobotHooverServiceResponse robotHooverResponse = robotHooverService.runHoover(roboHooverRequest);
        return mapToRoboHooverResponse(robotHooverResponse);
    }

    private RobotHooverServiceRequest mapToRequest(RobotHooverRequest robotHooverRequest) {
        List<Position> patches = createPatches(robotHooverRequest.getPatches());
        return new RobotHooverServiceRequest(buildRoomPosition(robotHooverRequest),
                buildHooverPosition(robotHooverRequest), patches, robotHooverRequest.getInstructions());
    }

    private List<Position> createPatches(List<List<Integer>> patches) {
        return patches.stream()
                .map(patch -> new Position(patch.get(0), patch.get(1)))
                .collect(toList());
    }

    private void validateRequest(RobotHooverRequest roboHoverRequest) {
        validateRoomCoordinates(roboHoverRequest);
        validateHooverCoordinates(roboHoverRequest);
    }

    private void validateHooverCoordinates(RobotHooverRequest robotHooverRequest) {
        boolean isValidHooverCoordinates = nonNull(robotHooverRequest.getHooverCoordinates())
                && !robotHooverRequest.getHooverCoordinates().isEmpty()
                && robotHooverRequest.getHooverCoordinates().size() == 2
                && robotHooverRequest.getHooverCoordinates().get(0) >= 0
                && robotHooverRequest.getHooverCoordinates().get(1) >= 0;
        if (!isValidHooverCoordinates) {
            throw new InvalidInputException("Invalid hoover coordinates");
        }
    }

    private void validateRoomCoordinates(RobotHooverRequest robotHooverRequest) {
        boolean isValidRoomCoordinates = nonNull(robotHooverRequest.getRoomSize())
                && !robotHooverRequest.getRoomSize().isEmpty()
                && robotHooverRequest.getRoomSize().size() == 2
                && robotHooverRequest.getRoomSize().get(0) >= 0
                && robotHooverRequest.getRoomSize().get(1) >= 0;

        if (!isValidRoomCoordinates) {
            throw new InvalidInputException("Invalid Room coordinates");
        }
    }

    private RobotHooverResponse mapToRoboHooverResponse(RobotHooverServiceResponse robotHooverResponse) {
        RobotHooverResponse roboHooverResponse = new RobotHooverResponse();
        roboHooverResponse.setHooverCoordinates(
                asList(robotHooverResponse.getPosition().getX(), robotHooverResponse.getPosition().getY()));
        roboHooverResponse.setCount(robotHooverResponse.getPatchesCount());
        return roboHooverResponse;
    }

    private Position buildHooverPosition(RobotHooverRequest robotHooverRequest) {
        List<Integer> hooverCoordinates = robotHooverRequest.getHooverCoordinates();
        return new Position(hooverCoordinates.get(0), hooverCoordinates.get(1));
    }

    private Position buildRoomPosition(RobotHooverRequest robotHooverRequest) {
        List<Integer> roomSize = robotHooverRequest.getRoomSize();
        return new Position(roomSize.get(0), roomSize.get(1));
    }
}
