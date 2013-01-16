/**
 *
 */
package cs3114.gis.core.util;

import static java.lang.Integer.*;

import cs3114.gis.core.GISRecord;
import cs3114.gis.core.Position;

/**
 * Parse a string into GIS record or a Position object
 *
 * @author Tianyu Geng
 * @version Nov 18, 2012
 */
public class GISRecordParser {

    /**
     * Parse the string into a GIS record.
     *
     * @param record
     * @return the GIS record
     */
    public static GISRecord parse(String record) {
        String[] strs = record.split("\\|");
        int id = Integer.parseInt(strs[0]);
        String name = strs[1];
        String featureClass = strs[2];
        String state = strs[3];
        int stateCode = Integer.parseInt(strs[4]);
        String country = strs[5];
        int countryCode = Integer.parseInt(strs[6]);
        Position primary = null;
        Position source = null;
        try {
            primary = parsePositionDMS(strs[8], strs[7]);
            source = parsePositionDMS(strs[12], strs[11]);
        }
        catch (FailToParsePositionException e) {
            // since the primary can be unknown and the source position is
            // optional, it's possible that the parsing fails.
        }
        int ftElevation;

        try {
            ftElevation = Integer.parseInt(strs[16]);
        }
        catch (Exception e) {
            // This field is optional so they can fail.
            ftElevation = Integer.MIN_VALUE;
        }
        String mapName = strs[17];
        String dateCreated = strs[18];
        String dateEdited = "";
        // if the last piece of information is lost, the last piece of
        // information is ignored
        if (strs.length == 20) {

            dateEdited = strs[19];
        }
        return new GISRecord(id, name, featureClass, state, stateCode,
                country, countryCode, primary, source, ftElevation,
                mapName, dateCreated, dateEdited);
    }

    /**
     * Parse a position string in the DDDMMSSd(E/W), DDMMSSd(N/S) format into a
     * Position object
     *
     * @param longitudeDMS
     * @param latitudeDMS
     * @return the position object
     * @throws FailToParsePositionException
     */
    public static Position parsePositionDMS(String longitudeDMS,
            String latitudeDMS) throws FailToParsePositionException {
        try {
            int latitude =
                    parseInt(latitudeDMS.substring(0, 2)) * 3600
                            + parseInt(latitudeDMS.substring(2, 4)) * 60
                            + parseInt(latitudeDMS.substring(4, 6));
            int longitudeOffset;
            // the longitude may have a leading zero for the degree or not, so
            // the following if statement will address for both case.
            if (longitudeDMS.length() == 8) {
                longitudeOffset = 1;
            }
            else {
                longitudeOffset = 0;
            }
            int longitude =
                    parseInt(longitudeDMS.substring(0,
                            longitudeOffset + 2))
                            * 3600
                            + parseInt(longitudeDMS.substring(
                                    longitudeOffset + 2,
                                    longitudeOffset + 4))
                            * 60
                            + parseInt(longitudeDMS.substring(
                                    longitudeOffset + 4,
                                    longitudeOffset + 6));
            longitude *=
                    longitudeDMS.charAt(longitudeOffset + 6) == 'E' ? 1
                            : -1;
            latitude *= latitudeDMS.charAt(6) == 'N' ? 1 : -1;
            return new Position(longitude, latitude);
        }

        catch (Exception e) {
            throw new FailToParsePositionException();
        }
    }

    /**
     * Thrown when parsing a position failed.
     *
     * @author Tianyu Geng
     * @version Nov 28, 2012
     */
    public static class FailToParsePositionException extends Exception {
        // nothing needs to be done here
    }

}
