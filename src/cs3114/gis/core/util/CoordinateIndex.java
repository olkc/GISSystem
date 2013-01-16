/**
 *
 */
package cs3114.gis.core.util;

import java.io.PrintWriter;

import cs3114.gis.container.prquad.prQuadtree.ElementIsOutsideTheWorldException;

import cs3114.gis.core.Position;

import cs3114.gis.container.prquad.prQuadtree;

import java.util.Vector;

/**
 * The class that manage the coordinate index. It's an adapter and has a
 * reference to the underlying prQuadtree.
 *
 * @author Tianyu Geng
 * @version Nov 17, 2012
 */
public class CoordinateIndex {
    private prQuadtree<CoordinateEntry> index;

    /**
     * Constructor for CoordinateIndex with the specified world size.
     *
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     */
    public CoordinateIndex(long xMin, long xMax, long yMin, long yMax) {
        index = new prQuadtree<CoordinateEntry>(xMin, xMax, yMin, yMax);
    }

    /**
     * Insert a new position offset pair into the index
     *
     * @param pos
     * @param offset
     * @return true if the insertion is successful, otherwise faluse.
     */
    public boolean insert(Position pos, long offset) {
        if (pos == null)
            return false;
        CoordinateEntry duplicate;
        try {
            // if the insert returns an object, it means it finds an duplicate
            // duiring insertion. In this case, it should put the offsets into
            // this duplicated object.
            if ((duplicate =
                    index.insert(new CoordinateEntry(pos, offset))) != null) {
                duplicate.addOffset(offset);
            }
        }
        catch (ElementIsOutsideTheWorldException e) {
            return false;
        }
        return true;
    }

    /**
     * Find all the offsets matching a position
     *
     * @param pos
     * @return the collection of offsets matching the position
     */
    public Vector<Long> find(Position pos) {
        CoordinateEntry entry = index.find(new CoordinateEntry(pos));
        if (entry == null) {
            return null;
        }
        return entry.getOffsets();
    }

    /**
     * Find all the offsets inside a certain area
     *
     * @param pos
     * @param halfX
     * @param halfY
     * @return the collection of all offsets inside the given area
     */
    public Vector<Long> find(Position pos, int halfX, int halfY) {
        Vector<CoordinateEntry> entries =
                index.find(pos.getLongitude() - halfX, pos.getLongitude()
                        + halfX, pos.getLatitude() - halfY,
                        pos.getLatitude() + halfY);

        if (entries == null || entries.isEmpty()) {
            return null;
        }
        // collection of all offsets found
        Vector<Long> result = new Vector<Long>();
        for (CoordinateEntry entry : entries) {
            result.addAll(entry.getOffsets());
        }

        return result;

    }

    /**
     * Get the size of the world.
     *
     * @return the array of longs which specify the size of the world.
     */
    public long[] getWorldSize() {
        return index.getWorldSize();
    }

    /**
     * Log the content of the coordinate index with a PrintWriter
     * @param log the PrintWriter to write the content to
     */
    public void logString(PrintWriter log) {
        index.logString(log);
    }

}
