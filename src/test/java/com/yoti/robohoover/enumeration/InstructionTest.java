package com.yoti.robohoover.enumeration;

import com.yoti.robohoover.exception.InvalidInstructionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InstructionTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowInvalidInstructionException_givenInvalidInstruction() {
        expectedException.expect(InvalidInstructionException.class);
        expectedException.expectMessage("No matching found for given Instruction A");
        Instruction.getInstruction('A');

    }
}