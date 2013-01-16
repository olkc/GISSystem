/**
 *
 */
package cs3114.gis.core.commands;

import cs3114.gis.core.util.GISRecordParser.FailToParsePositionException;

import cs3114.gis.core.Position;

import cs3114.gis.core.util.GISRecordParser;

import java.util.Vector;

/**
 * The what_is_in command
 *
 * @author Tianyu Geng
 * @version Nov 19, 2012
 */
public class WhatIsIn extends Command {

    @Override
    public String getName() {
        return "what_is_in";
    }

    @Override
    public void exec(String[] params) {
        try {
            // the center of this search
            Position center;
            // the details of level to log
            int detailLevel;
            // the horizontal and vertical 'radius' of this rectangular
            // searching area
            int halfX, halfY;

            if (params.length == 5) {
                center =
                        GISRecordParser.parsePositionDMS(params[2],
                                params[1]);
                halfX = Integer.parseInt(params[4]);
                halfY = Integer.parseInt(params[3]);
                detailLevel = LOG_NAME | LOG_STATE | LOG_POSITION;
            }
            else {
                center =
                        GISRecordParser.parsePositionDMS(params[3],
                                params[2]);
                halfX = Integer.parseInt(params[5]);
                halfY = Integer.parseInt(params[4]);
                if (params[1].equals("-c"))
                    detailLevel = LOG_NOTHING;
                else
                    detailLevel = LOG_ALL;

            }

            log.println("In region (" + center.getLatitudeS() + " +/-"
                    + halfY + "\", " + center.getLongitudeS() + " +/-"
                    + halfX + "\").");
            Vector<Long> offsets = getCoordinateIndex().find(center, halfX, halfY);
            logByOffsets(offsets, detailLevel);
        }
        catch (FailToParsePositionException e) {
            log.println("Cannot read the position given.");
        }

    }
}
