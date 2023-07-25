package com.example.battleship.model.ship;

/**
 * @author Mahdi Sharifi
 * Represent the type of ship and create an instance of ship based on our type of ship.
 */
public enum ShipType {
    AIRCRAFT_CARRIER(5) {
        @Override
        public Ship newInstance() {
            return new AircraftCarrier();
        }
    },
    BATTLESHIP(4) {
        @Override
        public Ship newInstance() {
            return new Battleship();
        }
    },
    SUBMARINE(3) {
        @Override
        public Ship newInstance() {
            return new Submarine();
        }
    },
    CRUISER(3) {
        @Override
        public Ship newInstance() {
            return new Cruiser();
        }
    }, DESTROYER(2) {
        @Override
        public Ship newInstance() {
            return new Destroyer();
        }
    };

    // Create a new ship by ship type
    public abstract Ship newInstance();

    private final int length;

    public int getLength() {
        return length;
    }

    ShipType(int length) {
        this.length = length;
    }


    public String getName() {
        return name();
    }

    public Character getFirstCharacterOfType() {
        return this.name().toUpperCase().charAt(0);
    }

}
