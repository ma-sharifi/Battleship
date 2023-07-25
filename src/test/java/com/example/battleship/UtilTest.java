package com.example.battleship;

import com.example.battleship.model.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mahdi Sharifi
 */
class UtilTest {

    @Test
    void coordinateToLabel() {
        assertEquals("A1", new Coordinate(0, 0).toLabel());
        assertEquals("B2", new Coordinate(1, 1).toLabel());
    }

    @Test
    void labelToCoordinate() {
        assertEquals(Coordinate.fromLabel("A1"), new Coordinate(0, 0));
        assertEquals(Coordinate.fromLabel("B2"), new Coordinate(1, 1));
    }

}