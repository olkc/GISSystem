/**
 *
 */
package cs3114.gis.core.commands;

import cs3114.gis.core.util.GISRecordParser.FailToParsePositionException;

import cs3114.gis.core.Position;

import cs3114.gis.core.util.GISRecordParser;

import java.util.Vector;

/**
 * The what_is_at command
 *
 * @author Tianyu Geng
 * @version Nov 19, 2012
 */
public class WhatIsAt extends Command {

    @Override
    public String getName() {
        return "what_is_at";
    }

    @Override
    public void exec(String[] params) {
        // the position for this search
        Position pos;
        // the level of details required
        int detailLevel;
        try {
            if (params.length == 3) {
                pos =
                        GISRecordParser.parsePositionDMS(params[2],
                                params[1]);
                detailLevel = LOG_NAME | LOG_COUNTY | LOG_STATE;
            }
            else {
                pos =
                        GISRecordParser.parsePositionDMS(params[3],
                                params[2]);
                if (params[1].equals("-c"))
                    detailLevel = LOG_NOTHING;
                else
                    detailLevel = LOG_ALL;
            }
            Vector<Long> offsets = getCoordinateIndex().find(pos);
            logByOffsets(offsets, detailLevel);
        }
        catch (FailToParsePositionException e) {
            log.println("Cannot parse the position to search.");
        }
    }
}
