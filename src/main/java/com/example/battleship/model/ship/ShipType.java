package com.example.battleship.model.ship;

/**
 * @author Mahdi Sharifi
 * Represent the type of ship and create an instance of ship based on our type of ship.
 */
public enum ShipType implements ShipCreator {
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

    private final int length;

    public int length() {
        return length;
    }

    ShipType(int length) {
        this.length = length;
    }

    public Character id() {
        return this.name().toUpperCase().charAt(0);
    }

    public static ShipType getTypeByShipId(Character shipId){
        for (ShipType type:ShipType.values()) {
            if(type.id().equals(shipId))
                return type;
        }
        return ShipType.BATTLESHIP;
    }
}
