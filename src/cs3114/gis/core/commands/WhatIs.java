/**
 *
 */
package cs3114.gis.core.commands;

import java.util.Vector;

/**
 * The what_is command
 *
 * @author Tianyu Geng
 * @version Nov 19, 2012
 */
public class WhatIs extends Command {

    @Override
    public String getName() {
        return "what_is";
    }

    @Override
    public void exec(String[] params) {
        Vector<Long> offsets;
        int detailLevel;
        if (params.length == 3) {
            offsets = nindex.find(params[1], params[2]);
            detailLevel = LOG_COUNTY | LOG_POSITION;
        }
        else {
            offsets = nindex.find(params[2], params[3]);
            if (params[1].equals("-c"))
                detailLevel = LOG_NOTHING;
            else
                detailLevel = LOG_ALL;
        }

        logByOffsets(offsets, detailLevel);

    }

}
