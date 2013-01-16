/**
 *
 */
package cs3114.gis.core.commands;

import cs3114.gis.core.GISRecord;

import cs3114.gis.core.util.GISRecordParser;

import java.io.IOException;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.BufferedReader;

/**
 *
 * @author tony1
 * @version Nov 18, 2012
 */
public class Import extends Command {

    /**
     *
     */
    @Override
    public String getName() {
        return "import";

    }

    /**
     * @param params
     */
    @Override
    public void exec(String[] params) {
        // create a reader for the file containing the records to import
        BufferedReader records;
        try {
            records = new BufferedReader(new FileReader(params[1]));
        }
        catch (FileNotFoundException e) {
            log.println("ERROR: GIS record file'" + params[1]
                    + "' not found.");
            return;
        }
        // String to store the current line
        String line;
        // the counter for the number of records in the name index
        int ncounter = 0;
        // the counter for the number of records in the coordinate index
        int ccounter = 0;
        try {
            records.readLine();
            storage.seekToEnd();
            // read all the lines in the record file
            while ((line = records.readLine()) != null) {
                // the current record
                GISRecord r = GISRecordParser.parse(line);
                // the offset at which this record is put into the database.
                long offset = storage.putGISRecord(line);
                // if the record is inserted into the coordinate index
                // successfully, increase the counter
                if (getCoordinateIndex().insert(r.getPosition(), offset))
                    ccounter++;
                // insertion for the name index will always be susccessful
                nindex.insert(r.getName(), r.getState(), offset);
                ncounter++;
            }

        }
        catch (IOException e) {
            log.println("ERROR: IOException while reading the GIS records");
            e.printStackTrace();
        }
        log.println("Indexed " + ncounter + " records in the name index");
        log.println("Longest probe sequence: "
                + nindex.getLongestProbing());
        log.println("Indexed " + ccounter
                + " records in the coordinate index");
        log.flush();
    }

}
