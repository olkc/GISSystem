/**
 *
 */
package cs3114.gis.core;

/**
 * The classs representing the GOSRecord.
 *
 * @author Tianyu Geng
 * @version Nov 17, 2012
 */
public class GISRecord {
    private int id;
    private String name;
    private String featureClass;
    private String state;
    @SuppressWarnings("unused")
    private int stateCode;
    private String county;
    @SuppressWarnings("unused")
    private int countyCode;
    private Position featurePos;
    private Position srcPos;
    private int ftElevation;
    private String mapName;
    private String dateCreated;
    private String dateEdited;

    /**
     * Constructor for GISRecord
     *
     * @param id
     * @param name
     * @param featureClass
     * @param state
     * @param stateCode
     * @param country
     * @param countryCode
     * @param featurePosition
     * @param sourcePosition
     * @param ftElevation
     * @param mapName
     * @param dateCreated
     * @param dateEdited
     */
    public GISRecord(int id, String name, String featureClass,
            String state, int stateCode, String country, int countryCode,
            Position featurePosition, Position sourcePosition,
            int ftElevation, String mapName, String dateCreated,
            String dateEdited) {
        this.id = id;
        this.name = name;
        this.featureClass = featureClass;
        this.state = state;
        this.stateCode = stateCode;
        this.county = country;
        this.countyCode = countryCode;
        this.featurePos = featurePosition;
        this.srcPos = sourcePosition;

        this.ftElevation = ftElevation;
        this.mapName = mapName;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;

    }

    /**
     * Get the position of the current record in string format but with all
     * significant information
     *
     * @return the string representtation of the current record in a human
     *         readable format.
     */
    public String toStringVerbose() {
        StringBuilder sb = new StringBuilder();
        sb.append("   Feature ID   : " + id);
        sb.append("\n   Feature Name : " + name);
        sb.append("\n   Feature Class: " + featureClass);
        sb.append("\n   State        : " + state);
        sb.append("\n   County       : " + county);
        if (featurePos != null) {
            sb.append("\n   Latitude     : " + featurePos.getLatitudeS());
            sb.append("\n   Longitude    : " + featurePos.getLongitudeS());
        }else {
            sb.append("\n   Latitude     : UNKNOWN\n   Longitude    : UNKNOWN");

        }
        if (srcPos != null) {
            sb.append("\n   Src Latitude : " + srcPos.getLatitudeS());
            sb.append("\n   Src Longitude: " + srcPos.getLongitudeS());
        }
        if (ftElevation != Integer.MIN_VALUE) {
            sb.append("\n   Elev in ft   : " + ftElevation);
        }
        sb.append("\n   USGS Quad    : " + mapName);
        sb.append("\n   Date Created : " + dateCreated);
        if (dateEdited != null && !dateEdited.isEmpty()) {
            sb.append("\n   Date Edited  : " + dateEdited);
        }
        return sb.toString();

    }

    /**
     * Get the position of the current record in string format.
     *
     * @return the string representing of the coordinate
     */
    public String toString() {
        if (featurePos != null) {
            return name + " \t" + county + " \t" + state + "\t"
                    + featurePos.getLatitudeS() + "  "
                    + featurePos.getLongitudeS();
        }
        return name + " \t" + county + " \t" + state;
    }

    /**
     * Get the position feature coordinate by a Position object.
     *
     * @return the Position object for the feature coordinate
     */
    public Position getPosition() {
        return featurePos;
    }

    /**
     * Get the name of this feature.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the state abbreviation of this feature
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Get the county name.
     *
     * @return the county name
     */
    public String getCounty() {
        return county;
    }

    /**
     * Get the string representation of the primary coordinate.
     *
     * @return the primary coordinate
     */
    public String getPrimaryPosString() {
        return featurePos.getLatitudeS() + "  "
                + featurePos.getLongitudeS();
    }

}
