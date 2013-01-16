/**
 *
 */
package cs3114.gis.core;

import cs3114.gis.container.prquad.Compare2D;

import cs3114.gis.container.prquad.Direction;

/**
 * This is a class to represent a geo-coordinate. It implements the Compare2D
 * interface so it can be used in the prQuadtree.
 *
 * @author Tianyu Geng
 * @version Nov 17, 2012
 */
public class Position implements Compare2D<Position> {
    /**
     * The latitude in second
     */
    protected int latitude;
    /**
     * The longitude in second
     */
    protected int longitude;

    /**
     * Constructor for Position
     * @param longitude in second
     * @param latitude in second
     */
    public Position(int longitude, int latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the latitude in second
     */
    public int getLatitude() {
        return latitude;
    }

    /**
     * @return longitude in second
     */
    public int getLongitude() {
        return longitude;
    }

    public Direction directionFrom(double X, double Y) {
        if (longitude == X && latitude == Y) {
            return Direction.NOQUADRANT;
        }
        if (longitude > X) {
            if (latitude > Y) {
                return Direction.NE;
            }
            else {
                return Direction.SE;
            }
        }
        else {
            if (latitude > Y) {
                return Direction.NW;
            }
            else {
                return Direction.SW;
            }
        }
    }

    public Direction inQuadrant(double xLo, double xHi, double yLo,
            double yHi) {
        return directionFrom(((xLo + xHi) / 2), ((yLo + yHi) / 2));
    }

    public boolean inBox(double xLo, double xHi, double yLo, double yHi) {
        return longitude <= xHi && longitude >= xLo && latitude <= yHi
                && latitude >= yLo;
    }

    public String toString() {
        return "(" + getLongitude() + ", " + getLatitude() + ")";
    }

    public boolean equals(Object o) {
        if (Position.class.equals(o.getClass())) {
            Position other = (Position) o;
            return directionFrom(other.longitude, other.latitude) == Direction.NOQUADRANT;
        }
        return false;
    }

    /**
     * Get the latitude in string format
     * @return a string representing the latitude.
     */
    public String getLatitudeS() {
        int absL = Math.abs(latitude);
        return (absL / 3600) + "°" + ((absL / 60) % 60) + "\'"
                + (absL % 60) + "\"" + (latitude > 0 ? "N" : "S");
    }

    /**
     * Get the longitude in a string format
     * @return the string representing the longitude
     */
    public String getLongitudeS() {
        int absL = Math.abs(longitude);
        return (absL / 3600) + "°" + ((absL / 60) % 60) + "\'"
                + (absL % 60) + "\"" + (longitude > 0 ? "E" : "W");
    }


    @Override
    public long getX() {
        return longitude;
    }

    @Override
    public long getY() {
        return latitude;
    }
}
