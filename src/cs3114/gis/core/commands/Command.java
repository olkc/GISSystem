/**
 *
 */
package cs3114.gis.core.commands;

import cs3114.gis.Control;

import cs3114.gis.core.GISRecord;
import cs3114.gis.core.util.CoordinateIndex;
import cs3114.gis.core.util.DataStorage;
import cs3114.gis.core.util.NameIndex;
import java.util.Vector;
import java.util.List;
import java.util.Arrays;
import java.io.RandomAccessFile;
import java.io.PrintWriter;

/**
 * The base class for all commands supported by this program
 *
 * @author Tianyu Geng
 * @version Nov 17, 2012
 */
public abstract class Command {
    /**
     * Only log the number of records found.
     */
    public static final int LOG_NOTHING = 0;
    /**
     * Log the name of the feature.
     */
    public static final int LOG_NAME = 1;
    /**
     * Log the county name of the feature.
     */
    public static final int LOG_COUNTY = 1 << 1;
    /**
     * Log the state abbreviation of the feature.
     */
    public static final int LOG_STATE = 1 << 2;
    /**
     * Log the position of the feature.
     */
    public static final int LOG_POSITION = 1 << 3;
    /**
     * Log every non-empty detail for the feature
     */
    public static final int LOG_ALL = -1;
    /**
     * The coordinate index that can retrieve offsets using a coordinate
     */
    private static CoordinateIndex cindex;
    /**
     * The name index that can retrieve offsets using a name
     */
    protected static NameIndex nindex;
    /**
     * The storage that can retrieve a GIS record using an offset
     */
    protected static DataStorage storage;
    /**
     * The printWriter that will print to the log file
     */
    protected static PrintWriter log;

    /**
     * The list of all available command classes. This list is used to generate
     * all the command objects at run time.
     */
    @SuppressWarnings("unchecked")
    public static List<Class<? extends Command>> commands =
            Arrays.asList(Import.class, World.class, WhatIsAt.class,
                    Debug.class, WhatIs.class, WhatIsIn.class, Quit.class);

    /**
     * Initialize the three static fields other than the coordinate index, which
     * has to be initialized with the "world" command.
     *
     * @param database
     *            the file on the hard drive that is used as the database
     * @param log
     *            the file on the hard drive to log all the commands and outputs
     */
    static public void initialize(RandomAccessFile database,
            @SuppressWarnings("hiding") PrintWriter log) {

        nindex = new NameIndex();
        storage = new DataStorage(database);
        Command.log = log;
    }

    /**
     * Get the command name. This name is used at run time to differentiate
     * different commands.
     *
     * @return the name of the command
     */
    public abstract String getName();

    /**
     * Execute the command with the given parameters.
     *
     * @param params
     */
    public abstract void exec(String[] params);

    /**
     * Print a line of string in the log file.
     *
     * @param line
     */
    public static void echoln(String line) {
        log.println(line);
    }

    /**
     * Convenient method to log the records by giving a collection of offsets.
     *
     * @param offsets
     *            the offsets of records that will be logged
     * @param detailLevel
     *            LOG_NOTHING will only log the number of records;
     *
     *            LOG_NAME will log the feature name;
     *
     *            LOG_STATE will log the state abbreviate of the feature;
     *
     *            LOG_COUNTY will log the name of the county of the feature;
     *
     *            LOG_POSITION will log the position of the feature;
     *
     *            LOG_ALL will log everything.
     *
     *            NOTE: LOG_NAME, LOG_STATE, LOG_COUNTY, and LOG_POSITION can be
     *            used together will the bit-wire or operator
     */
    protected void logByOffsets(Vector<Long> offsets, int detailLevel) {
        if (offsets == null) {
            log.println("Nothing is found.");
            return;
        }
        log.println("Find " + offsets.size() + " records.");
        if (detailLevel == 0) {
            return;
        }
        // print the details required by according to the detail level for all
        // the GIS records found.
        for (long offset : offsets) {
            GISRecord record = storage.getGISRecord(offset);
            if (detailLevel == LOG_ALL) {
                log.println("\nFound mathcing record at offset " + offset);
                log.println(record.toStringVerbose());
                continue;
            }
            // log the details according to the detail level.
            log.print(offset + ":");
            if ((detailLevel & LOG_NAME) != 0) {
                log.print("\t" + record.getName());
            }
            if ((detailLevel & LOG_COUNTY) != 0) {
                log.print("\t" + record.getCounty());
            }
            if ((detailLevel & LOG_STATE) != 0) {
                log.print("\t" + record.getState());
            }
            if ((detailLevel & LOG_POSITION) != 0) {
                log.print("\t" + record.getPrimaryPosString());
            }
            log.println();

        }

        log.flush();
    }

    /**
     * Get the coordinate index.
     *
     * @return the coordinate index
     */
    static protected CoordinateIndex getCoordinateIndex() {
        if (cindex == null) {
            log.println("Please use command \"world\t<xMin>\t<xMax>\t<yMin>\t<yMax>\" as the first command in the script.");
            Control.exit();
        }
        return cindex;
    }

    /**
     * Set the coordinate index.
     *
     * @param index
     *            the index to set to the coordinate index.
     */
    static protected void setCoordinateIndex(CoordinateIndex index) {
        cindex = index;
    }
}
