/**
 *
 */
package cs3114.gis.core.commands;

import cs3114.gis.core.util.GISRecordParser.FailToParsePositionException;

import cs3114.gis.Control;

import cs3114.gis.core.Position;

import cs3114.gis.core.util.CoordinateIndex;
import cs3114.gis.core.util.GISRecordParser;

/**
 * The world command.
 *
 * @author Tianyu Geng
 * @version Nov 22, 2012
 */
public class World extends Command {

    @Override
    public String getName() {
        return "world";
    }

    @Override
    public void exec(String[] params) {
        try {

            Position leftBot =
                    GISRecordParser
                            .parsePositionDMS(params[1], params[3]);
            Position rightTop =
                    GISRecordParser
                            .parsePositionDMS(params[2], params[4]);
            setCoordinateIndex(new CoordinateIndex(
                    leftBot.getLongitude(), rightTop.getLongitude(),
                    leftBot.getLatitude(), rightTop.getLatitude()));
            log.println("Worlds boundaries are set to:");
            log.println("                " + rightTop.getLatitudeS());
            log.println(leftBot.getLongitudeS()
                    + "                        "
                    + rightTop.getLongitudeS());
            log.println("                " + leftBot.getLatitudeS());
        }

        catch (ArrayIndexOutOfBoundsException e) {
            log.println("There should be 4 parameters after command 'world'.");
            Control.exit();
        }
        catch (FailToParsePositionException e) {
            log.println("The parameters after world should be 'west latitude', 'east latitude', 'south longitude', and 'north longitude'.");
            Control.exit();
        }

    }
}
