package cs3114.gis.container.prquad;

import java.io.PrintWriter;

import java.util.Vector;

/**
 * A container class to store objects that extend Compare2D and supports fast
 * find.
 *
 * @author Tianyu Geng
 * @version Nov 22, 2012
 * @param <T>
 *            The type of object stored in the pr-quad tree.
 */
public class prQuadtree<T extends Compare2D<? super T>> {
    /**
     * When the element inserted is outside the world boundary, this exception
     * is thrown
     *
     * @author Tianyu Geng
     * @version Nov 23, 2012
     */
    public static class ElementIsOutsideTheWorldException extends
            Exception {
        // nothing
    }

    /**
     * The internal node that has four children.
     *
     * @author Tianyu Geng
     * @version Nov 22, 2012
     */
    class prQuadInternal extends prQuadNode {

        /**
         * the north west pointer
         */
        prQuadNode NW;
        /**
         * the north east pointer
         */
        prQuadNode NE;
        /**
         * the south east pointer
         */
        prQuadNode SE;
        /**
         * the south west pointer
         */
        prQuadNode SW;

        /**
         * Constructor for prQuadInternal
         */
        public prQuadInternal() {
            NW = null;
            NE = null;
            SE = null;
            SW = null;
        }
    }

    /**
     * The leaf node that stores individual elements
     *
     * @author Tianyu Geng
     * @version Nov 22, 2012
     */
    class prQuadLeaf extends prQuadNode {

        /**
         * The collecton of elements stored in the leaf
         */
        Vector<T> Elements;

        /**
         * Constructor for prQuadLeaf
         *
         * @param elem
         *            The element to be added into this leaf node
         */
        public prQuadLeaf(T elem) {
            Elements = new Vector<T>(BUCKET_SIZE);
            Elements.add(elem);
        }

    }

    /**
     * The abstract class for both internal node and leaf node.
     *
     * @author Tianyu Geng
     * @version Nov 22, 2012
     */
    abstract class prQuadNode {
        // intended to be empty
    }

    /**
     * The size of the collection of elements in each leaf node.
     */
    public static final int BUCKET_SIZE = 4;

    private prQuadNode root;

    private long xMin, xMax, yMin, yMax;
    private T insertedT;

    private String prefix;

    /**
     * Constructor for prQuadtree.
     *
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     */
    public prQuadtree(long xMin, long xMax, long yMin, long yMax) {
        this.xMax = xMax;
        this.xMin = xMin;
        this.yMax = yMax;
        this.yMin = yMin;
        root = null;

    }

    /**
     * Regional find that returns all elements in a rectangular region.
     *
     * @param xLo
     * @param xHi
     * @param yLo
     * @param yHi
     * @return the collections of elements inside the region
     */
    public Vector<T> find(long xLo, long xHi, long yLo, long yHi) {
        // This vector will contain all the records found and be returned at the
        // end.
        Vector<T> result = new Vector<T>();

        regionHelper(result, root, xMin, xMax, yMin, yMax, xLo, xHi, yLo,
                yHi);
        return result;
    }

    /**
     * Find the element that at the same position as the input element.
     *
     * @param Elem
     * @return the element that is at the same position as the input elements
     */
    public T find(T Elem) {

        return findHelper(root, Elem, xMin, xMax, yMin, yMax);

    }

    private T findHelper(prQuadNode node, T elem, double xl, double xh,
            double yl, double yh) {
        // the node is null means there is nothing in thi snode
        if (node == null) {
            return null;
        }
        // if the current node is a leaf node, traverse all the elements in its
        // collection to find match.
        if (node.getClass().equals(prQuadLeaf.class)) {
            @SuppressWarnings("unchecked")
            prQuadLeaf lnode = (prQuadLeaf) node;
            // traverse all the elements in this leaf node to find a match
            for (int i = 0; i < lnode.Elements.size(); i++) {
                if (lnode.Elements.get(i).directionFrom(elem.getX(),
                        elem.getY()) == Direction.NOQUADRANT) {
                    return lnode.Elements.get(i);
                }
            }
        }
        else {
            // if the current node is an internal node, determine where the
            // match should be at and call the same helper function recursively
            // to find a match.

            double xc = (xl + xh) / 2;
            double yc = (yl + yh) / 2;
            @SuppressWarnings("unchecked")
            prQuadInternal inode = (prQuadInternal) node;
            // determine the right place to continue finding for the match
            switch (elem.directionFrom(xc, yc)) {
                case NOQUADRANT:
                case NE:

                    return findHelper(inode.NE, elem, xc, xh, yc, yh);

                case NW:

                    return findHelper(inode.NW, elem, xl, xc, yc, yh);

                case SE:

                    return findHelper(inode.SE, elem, xc, xh, yl, yc);

                case SW:

                    return findHelper(inode.SW, elem, xl, xc, yl, yc);

            }
        }
        return null;

    }

    /**
     * Get the positions and the dimensions of the rectangular world.
     *
     * @return the array containing the positions of the four boundaries in the
     *         order of xMin, xMax, yMin, yMax
     */
    public long[] getWorldSize() {
        long[] result = { xMin, xMax, yMin, yMax };
        return result;
    }

    /**
     * Insert an element into the prQuadtree
     *
     * @param elem
     * @return return null if the element insertion is successful without any
     *         duplicates found. Otherwise, return the duplicated object in the
     *         tree
     * @throws ElementIsOutsideTheWorldException
     *             if the element is outside the world, throw the
     *             ElementIsOutsideTheWorldException
     */
    public T insert(T elem) throws ElementIsOutsideTheWorldException {
        insertedT = elem;
        // check whether the element is in the world.
        if (elem.getX() >= xMin && elem.getX() <= xMax
                && elem.getY() >= yMin && elem.getY() <= yMax) {
            root = insertHelper(root, elem, xMin, xMax, yMin, yMax);

        }
        else {
            throw new ElementIsOutsideTheWorldException();
        }
        return insertedT;

    }

    private prQuadNode insertHelper(prQuadNode node, T elem, double xl,
            double xh, double yl, double yh) {

        if (node == null) {
            insertedT = null;
            return new prQuadLeaf(elem);
        }

        Vector<T> elemsBuffer = new Vector<T>();
        elemsBuffer.add(elem);
        // if the current node is a leaf, try to first find a match; if a match
        // if found, just stop the insertion because no duplicates should be
        // allowed in the tree. Otherwise, continue doing the insertion by first
        // check whether the leaf can fit one more element. If yes, put the new
        // element in there and we are done; if no, put all the elements in the
        // leaf node and the new element in the elemsBuffer and treat the
        // current node as a internal node and do all the necessary operations
        // with an internal node.
        if (node.getClass().equals(prQuadLeaf.class)) {
            @SuppressWarnings("unchecked")
            prQuadLeaf lnode = (prQuadLeaf) node;
            // traverse the leaf node and if a duplicate is found, return the
            // duplicate and stop insertion.
            for (int i = 0; i < lnode.Elements.size(); i++) {
                if (lnode.Elements.get(i).directionFrom(elem.getX(),
                        elem.getY()) == Direction.NOQUADRANT) {
                    insertedT = lnode.Elements.get(i);
                    return lnode;
                }
            }
            // If there are still spaces left in this current node, put the new
            // element here.
            if (lnode.Elements.size() < BUCKET_SIZE) {
                lnode.Elements.add(elem);
                insertedT = null;
                return lnode;
            }

            elemsBuffer.addAll(lnode.Elements);
            // create a new internal node if the current leaf node cannot hold
            // more elements and treat the current node as this newly created
            // internal node and add all the elements to this internal node
            // later.
            prQuadInternal inode = new prQuadInternal();

            node = inode;
        }
        // Now we will work on the case when the node is an internal node.
        double xc = (xl + xh) / 2;
        double yc = (yl + yh) / 2;
        @SuppressWarnings("unchecked")
        prQuadInternal inode = (prQuadInternal) node;
        // traverse the elements in the buffer and put them into the correct
        // nodes.
        for (T aElem : elemsBuffer) {

            switch (aElem.directionFrom(xc, yc)) {
            // note in both find and insert, the nodes that falls at the center
            // of this sub-region will go to the north east child
                case NOQUADRANT:
                case NE:
                    inode.NE =
                            insertHelper(inode.NE, aElem, xc, xh, yc, yh);
                    break;
                case NW:
                    inode.NW =
                            insertHelper(inode.NW, aElem, xl, xc, yc, yh);
                    break;
                case SE:
                    inode.SE =
                            insertHelper(inode.SE, aElem, xc, xh, yl, yc);
                    break;
                case SW:
                    inode.SW =
                            insertHelper(inode.SW, aElem, xl, xc, yl, yc);
            }
        }
        return node;
    }

    // xl,xh,yl,yh represent: xLow, xHigh, yLow, yHigh respectively.
    // rxl, rxh, ryl, ryh represent: the boundaires of the region.
    // list is the collection of found elements in the specified region.
    // node is the current node this helper function is looking at.
    private void regionHelper(Vector<T> list, prQuadNode node, double xl,
            double xh, double yl, double yh, long rxl, long rxh,
            long ryl, long ryh) {
        if (node == null) {
            return;
        }
        // if the node is an leaf node, the determien whether it's inside the
        // region.
        if (node.getClass().equals(prQuadLeaf.class)) {
            @SuppressWarnings("unchecked")
            prQuadLeaf lnode = (prQuadLeaf) node;
            for (int i = 0; i < lnode.Elements.size(); i++) {
                if (lnode.Elements.get(i).inBox(rxl, rxh, ryl, ryh)) {
                    list.add(lnode.Elements.get(i));

                }
            }
        }
        else {
            // if the node is an internal node, determine which of it's sub node
            // intersects with the region and do the search on that region.
            @SuppressWarnings("unchecked")
            prQuadInternal inode = (prQuadInternal) node;

            double xc = (xl + xh) / 2;
            double yc = (yl + yh) / 2;
            // check whether the children intersect with the region; if so,
            // recursively search them
            if (xc <= rxh && xh >= rxl && yc <= ryh && yh >= ryl) {
                regionHelper(list, inode.NE, xc, xh, yc, yh, rxl, rxh,
                        ryl, ryh);
            }
            if (xl <= rxh && xc >= rxl && yc <= ryh && yh >= ryl) {
                regionHelper(list, inode.NW, xl, xc, yc, yh, rxl, rxh,
                        ryl, ryh);
            }
            if (xc <= rxh && xh >= rxl && yl <= ryh && yc >= ryl) {
                regionHelper(list, inode.SE, xc, xh, yl, yc, rxl, rxh,
                        ryl, ryh);
            }
            if (xl <= rxh && xc >= rxl && yl <= ryh && yc >= ryl) {
                regionHelper(list, inode.SW, xl, xc, yl, yc, rxl, rxh,
                        ryl, ryh);
            }

        }
    }

    /**
     * Log the content of this prQuadtree to the PrintWriter in the parameter
     *
     * @param log
     *            the parameter to log the content
     */
    public void logString(PrintWriter log) {
        // the prefix is the "|" or " " before each line
        prefix = "";
        log.println();
        toStringHelper(root, log, "ROOT");
    }

    /**
     * convert this tree into a human readable format on the command line.
     *
     * @param node
     *            the node to log the content
     * @param log
     *            the PrintWriter to write the content to
     * @param prefixSub
     *            the characters right after the prefix
     */
    private void toStringHelper(prQuadNode node, PrintWriter log,
            String prefixSub) {
        // print null for empty node
        if (node == null) {
            log.println(prefix + prefixSub + "null");
            return;
        }
        // if the node is a leaf, print the content in it.
        if (node.getClass().equals(prQuadLeaf.class)) {
            @SuppressWarnings("unchecked")
            prQuadLeaf lnode = (prQuadLeaf) node;
            log.println(prefix + prefixSub + lnode.Elements);
        }
        // if the node is an internal node, print a node line and its children
        if (node.getClass().equals(prQuadInternal.class)) {
            @SuppressWarnings("unchecked")
            prQuadInternal inode = (prQuadInternal) node;
            log.println(prefix + prefixSub);
            if (prefixSub.startsWith("├")) {
                prefix += "│ ";
            }
            else if (prefixSub.startsWith("└")) {
                prefix += "  ";
            }
            log.flush();
            toStringHelper(inode.NE, log, "├─NE ");
            toStringHelper(inode.NW, log, "├─NW ");
            toStringHelper(inode.SE, log, "├─SE ");
            toStringHelper(inode.SW, log, "└─SW ");
            if (prefix.length() > 0) {
                prefix = prefix.substring(0, prefix.length() - 2);
            }
        }

    }
}
