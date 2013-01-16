/**
 *
 */
package cs3114.gis.core.commands;

import cs3114.gis.Control;

/**
 * The quit command
 *
 * @author Tianyu Geng
 * @version Nov 19, 2012
 */
public class Quit extends Command {

    @Override
    public String getName() {
        return "quit";
    }

    @Override
    public void exec(String[] params) {
        log.println("Terminating all commands.");
        // Call the exit method in the main control class.
        Control.exit();

    }

}
