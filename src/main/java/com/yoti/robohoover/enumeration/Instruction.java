package com.yoti.robohoover.enumeration;

import com.yoti.robohoover.exception.InvalidInstructionException;

public enum Instruction {
    EAST('E'),
    WEST('W'),
    NORTH('N'),
    SOUTH('S');

    private char value;

    Instruction(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public static Instruction getInstruction(char ch) {
        for (Instruction d : Instruction.values()) {
            if (d.getValue() == ch) {
                return d;
            }
        }
        throw new InvalidInstructionException("No matching found for given Instruction " +  ch);
    }
}
