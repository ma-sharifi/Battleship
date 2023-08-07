package com.example.battleship.model;

import com.example.battleship.exception.WrongCoordinateException;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mahdi Sharifi
 */

@Data
public class Coordinate implements Comparable<Coordinate> {

    private int row;
    private int column;
    private boolean hit;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Coordinate(String label) {
        Coordinate coordinate=fromLabel(label);
        this.row = coordinate.row;
        this.column = coordinate.column;
    }

    public boolean isSameLocation(Coordinate otherCoordinate) {
        return row == otherCoordinate.getRow() && column == otherCoordinate.column;
    }

    /**
     * Convert Coordinate(1,1) to B2
     */
    public String toLabel() {
        return Character.toString(row+'A')+(column+1);
    }

    /**
     * Convert label A1 to Coordinate(0,0)
     */
    public static Coordinate fromLabel(String label) {//A1->(0,0)
        Pattern pattern = Pattern.compile("^([A-Z])(\\d+)$");
        Matcher matcher = pattern.matcher(label);

        if (!matcher.matches()) {
            throw new WrongCoordinateException(label);
        }

        String row = matcher.group(1);
        String column = matcher.group(2);

        int columnIndex =Integer.parseInt(column) - 1 ;
        int rowIndex =row.charAt(0) - 'A' ;
        return new Coordinate(rowIndex, columnIndex);
    }

    @Override
    public int compareTo(Coordinate otherCoordinate) {
        if(row==otherCoordinate.getRow() && column==otherCoordinate.column) return 0;
        return Integer.compare(row,otherCoordinate.row);
    }
}
