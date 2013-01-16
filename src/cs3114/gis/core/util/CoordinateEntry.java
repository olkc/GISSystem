/**
 *
 */
package cs3114.gis.core.util;

import cs3114.gis.core.Position;

import java.util.Vector;

/**
 * The entry stored in the coordiante index used by all the commands. It only
 * adds a collection of offsets to the Position class.
 *
 * @author Tianyu Geng
 * @version Nov 17, 2012
 */
public class CoordinateEntry extends Position {

    private Vector<Long> offsets;

    /**
     * Constructor for CoordinateEntry
     *
     * @param pos
     *            the position of this entry
     * @param offset
     *            the offset to be added at creation
     */
    public CoordinateEntry(Position pos, long offset) {
        super(pos.getLongitude(), pos.getLatitude());
        offsets = new Vector<Long>();
        this.offsets.add(offset);
    }

    /**
     * Constructor for CoordinateEntry
     *
     * @param pos
     *            the position of this entry
     */
    public CoordinateEntry(Position pos) {
        this(pos, 0);
    }

    public String toString() {
        return "(" + longitude + ", " + latitude + "):"
                + offsets.toString();
    }

    /**
     * Get all the offsets.
     *
     * @return the vector of all the offsets
     */
    public Vector<Long> getOffsets() {
        return offsets;
    }

    /**
     * Add a new offset to this entry
     *
     * @param offset
     */
    public void addOffset(long offset) {
        offsets.add(offset);
    }

}
