package com.example.battleship.util;

import com.example.battleship.exception.WrongCoordinateException;
import com.example.battleship.model.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mahdi Sharifi
 * @see Util
 */
class UtilTest {

    @Test
    void shouReturnA1_when00givenToCoordinateToLabel() {
        assertEquals("A1", new Coordinate(0, 0).toLabel());
        assertEquals("B2", new Coordinate(1, 1).toLabel());
    }

    @Test
    void shouldReturn00_whenA1GivenToFromLabel() {
        assertEquals(new Coordinate(0, 0),Coordinate.fromLabel("A1"));
        assertEquals(new Coordinate(1, 1),Coordinate.fromLabel("B2"));
    }
    @Test
    void shouldThrowWrongCoordinateException_whenA1kGivenToFromLabel() {
        var thrown= Assertions.assertThrows(WrongCoordinateException.class,()->{
           Coordinate.fromLabel("1k");
        });
        assertTrue(thrown.getMessage().startsWith("Coordinate is wrong: "));
    }

}