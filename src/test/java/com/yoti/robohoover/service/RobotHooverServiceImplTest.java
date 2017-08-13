package com.yoti.robohoover.service;

import com.yoti.robohoover.exception.InvalidInputException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RobotHooverServiceImplTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private RobotHooverService robotHooverService;

    @Before
    public void setUp() throws Exception {
        robotHooverService = new RobotHooverServiceImpl();
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAsZeroAndNoInstructions() {
        Position roomPosition = new Position(0, 0);
        Position hooverPosition = new Position(0, 0);
        List<Position> patches = emptyList();
        String instructions = null;
        Position expectedPosition = new Position(0, 0);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));


        assertThat(actualResponse.getPosition().getX(), is(expectedPosition.getX()));
        assertThat(actualResponse.getPosition().getY(), is(expectedPosition.getY()));
        assertThat(actualResponse.getPatchesCount(), is(0));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndInvalidInstructions() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(0, 0);
        List<Position> patches = emptyList();
        String instructions = "NEWSA";

        expectedException.expect(InvalidInputException.class);
        expectedException.expectMessage("Invalid instructions");

        RobotHooverServiceImpl robotHooverService = new RobotHooverServiceImpl();
        robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndMoveNorth() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(0, 0);
        List<Position> patches = emptyList();
        String instructions = "N";
        Position expectedPosition = new Position(0, 1);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(expectedPosition.getX()));
        assertThat(actualResponse.getPosition().getY(), is(expectedPosition.getY()));
        assertThat(actualResponse.getPatchesCount(), is(0));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndMoveEast() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(0, 0);
        List<Position> patches = emptyList();
        String instructions = "E";
        Position expectedPosition = new Position(1, 0);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(expectedPosition.getX()));
        assertThat(actualResponse.getPosition().getY(), is(expectedPosition.getY()));
        assertThat(actualResponse.getPatchesCount(), is(0));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndMoveWest() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(2, 2);
        List<Position> patches = emptyList();
        String instructions = "W";
        Position position = new Position(1, 2);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(position.getX()));
        assertThat(actualResponse.getPosition().getY(), is(position.getY()));
        assertThat(actualResponse.getPatchesCount(), is(0));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndMoveSouth() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(2, 2);
        List<Position> patches = emptyList();
        String instructions = "S";
        Position position = new Position(2, 1);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(position.getX()));
        assertThat(actualResponse.getPosition().getY(), is(position.getY()));
        assertThat(actualResponse.getPatchesCount(), is(0));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndMoveSouthEast() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(2, 2);
        List<Position> patches = emptyList();
        String instructions = "SE";
        Position position = new Position(3, 1);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(position.getX()));
        assertThat(actualResponse.getPosition().getY(), is(position.getY()));
        assertThat(actualResponse.getPatchesCount(), is(0));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndValidInstructions() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(1, 2);
        List<Position> patches = emptyList();
        String instructions = "NNESEESWNWW";
        Position position = new Position(1, 3);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(position.getX()));
        assertThat(actualResponse.getPosition().getY(), is(position.getY()));
        assertThat(actualResponse.getPatchesCount(), is(0));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndValidInstructionsAndSinglePatch() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(2, 2);
        List<Position> patches = singletonList(new Position(3, 1));
        String instructions = "SE";
        Position position = new Position(3, 1);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(position.getX()));
        assertThat(actualResponse.getPosition().getY(), is(position.getY()));
        assertThat(actualResponse.getPatchesCount(), is(1));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndValidInstructionsAndMultiplePatches() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(1, 2);
        List<Position> patches = asList(new Position(1, 0), new Position(2, 2), new Position(2, 3));
        String instructions = "NNESEESWNWW";
        Position position = new Position(1, 3);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(position.getX()));
        assertThat(actualResponse.getPosition().getY(), is(position.getY()));
        assertThat(actualResponse.getPatchesCount(), is(1));
    }

    @Test
    public void shouldReturnHooverPosition_givenInputCoordinatesAndRoomCoordinatesAndValidInstructionsAndMultiplePatches_WithMixedCaseInstructions() {
        Position roomPosition = new Position(5, 5);
        Position hooverPosition = new Position(1, 2);
        List<Position> patches = asList(new Position(1, 0), new Position(2, 2), new Position(2, 3));
        String instructions = "nNeSEESWNWw";
        Position position = new Position(1, 3);

        RobotHooverServiceResponse actualResponse = robotHooverService.runHoover(new RobotHooverServiceRequest(roomPosition, hooverPosition, patches, instructions));

        assertThat(actualResponse.getPosition().getX(), is(position.getX()));
        assertThat(actualResponse.getPosition().getY(), is(position.getY()));
        assertThat(actualResponse.getPatchesCount(), is(1));
    }
}
