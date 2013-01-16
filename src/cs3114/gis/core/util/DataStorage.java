/**
 *
 */
package cs3114.gis.core.util;

import cs3114.gis.core.GISRecord;

import java.io.IOException;

import java.io.RandomAccessFile;

import cs3114.gis.container.BufferPool;

/**
 * The abstraction of a data storage, it consists of the database on the hard
 * drive and the buffer pool for fast look up.
 *
 * @author Tianyu Geng
 * @version Nov 18, 2012
 */
public class DataStorage {
    private BufferPool<Long, GISRecord> pool;
    private RandomAccessFile raf;

    /**
     * Constructor for DataStorage
     *
     * @param database
     *            the database file that will be stored on the hard drive
     */
    public DataStorage(RandomAccessFile database) {
        this.raf = database;
        pool = new BufferPool<Long, GISRecord>();
    }

    /**
     * Get a GIS record with an offset
     *
     * @param offset
     * @return the GIS reocrd at the offset
     */
    public GISRecord getGISRecord(long offset) {
        // the result can be either parsed or retrieved from the buffer
        GISRecord result;
        String line = null;
        // first try to find from the buffer
        if ((result = pool.get(offset)) != null) {
            return result;
        }
        // if the buffer doesn't have the desired offset, try to retreive it
        // from the database file
        try {
            raf.seek(offset);
            line = raf.readLine();
            result = GISRecordParser.parse(line);
            pool.insert(offset, result);
        }
        catch (IOException e) {
            System.err
                    .println("Cannot read the database file, please restart the program.");
        }
        catch (NumberFormatException e) {
            System.err
                    .println("Database file is corrupted, please restart the program.");

        }
        return result;

    }

    /**
     * Move the file pointer to the end of the file so that putGISRecord will
     * work correctly.
     *
     * @throws IOException
     */
    public void seekToEnd() throws IOException {
        raf.seek(raf.length());
    }

    /**
     * Put a new GIS record to the database
     *
     * @param record
     * @return the offset at which this new record is put
     * @throws IOException
     */
    public long putGISRecord(String record) throws IOException {
        long offset = raf.getFilePointer();
        raf.writeBytes(record + "\n");
        return offset;
    }

    /**
     * Get the string that representing what is inside the buffer pool.
     *
     * @return the string representing what is inside the buffer pool
     */
    public String poolToString() {
        return pool.toString();
    }
}
