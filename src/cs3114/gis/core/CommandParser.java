/**
 *
 */
package cs3114.gis.core;

import java.io.IOException;

import java.util.TreeMap;

import cs3114.gis.core.commands.Command;

import java.io.PrintWriter;

import java.io.RandomAccessFile;

import java.io.BufferedReader;

/**
 * The class to parse the command in the scripts and call the corresponding
 * functions to handle the command.
 *
 * @author Tianyu Geng
 * @version Nov 19, 2012
 */
public class CommandParser {
    private BufferedReader scripts;
    private TreeMap<String, Command> cmdMap;
    private int count;

    /**
     * Constructor for CommandParser.
     *
     * @param scripts
     *            the BufferedReader containing all the commands
     * @param database
     *            the RandomAccessFile containing all the imported GIS records
     * @param log
     *            the PrintWriter to write the output
     */
    public CommandParser(BufferedReader scripts,
            RandomAccessFile database, PrintWriter log) {
        count = 0;
        this.scripts = scripts;
        Command.initialize(database, log);
        cmdMap = new TreeMap<String, Command>();
        // traverse the Command:commands and create a instance of all the
        // commands registered in the list.
        for (Class<? extends Command> cls : Command.commands) {
            Command aCmd;
            try {
                aCmd = cls.newInstance();
                cmdMap.put(aCmd.getName(), aCmd);
            }
            catch (InstantiationException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Parse the commands and call the corresponding functons to handle them.
     */
    public void parseAndExecute() {
        // the next line of commands
        String line;
        try {
            while ((line = scripts.readLine()) != null) {

                if (!line.startsWith(";") && !line.isEmpty()) {

                    Command.echoln("Command " + count++ + " : " + line);
                    String[] cmdparams = line.split("\t");
                    // get the command object with the command name
                    Command cmd = cmdMap.get(cmdparams[0]);
                    // if the command is not available
                    if (cmd == null) {
                        Command.echoln("\nCommand not exist.");
                    }
                    else {
                        Command.echoln("");
                        //execute the command with the parameters.
                        cmd.exec(cmdparams);
                    }
                    // print a spliter
                    Command.echoln("--------------------------------------------------------------");
                }
                else {
                    // echo the comment
                    Command.echoln(line);
                }
            }
        }
        catch (IOException e) {
            System.err.println("ERROR: Cannot read script file.");
        }
    }
}
