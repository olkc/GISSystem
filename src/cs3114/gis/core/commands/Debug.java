/**
 *
 */
package cs3114.gis.core.commands;

/**
 * The debug command
 *
 * @author Tianyu Geng
 * @version Nov 19, 2012
 */
public class Debug extends Command {

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public void exec(String[] params) {
        if (params[1].equals("quad")) {
            getCoordinateIndex().logString(log);
        }
        else if (params[1].equals("hash")) {
            nindex.logString(log);
        }
        else if (params[1].equals("pool")) {
            log.println(storage.poolToString());
        }
        log.flush();

    }

}
