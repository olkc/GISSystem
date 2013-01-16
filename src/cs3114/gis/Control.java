//    On my honor:
//
//    - I have not discussed the Java language code in my program with
//      anyone other than my instructor or the teaching assistants
//      assigned to this course.
//
//    - I have not used Java language code obtained from another student,
//      or any other unauthorized source, either modified or unmodified.
//
//    - If any Java language code or documentation used in my program
//      was obtained from another source, such as a text book or course
//      notes, that has been clearly noted with a proper citation in
//      the comments of my program.
//
//    - I have not designed this program in such a way as to defeat or
//      interfere with the normal operation of the Curator System.
//
//    Tianyu Geng
package cs3114.gis;

import java.text.SimpleDateFormat;

import java.io.IOException;

import java.io.File;

import java.io.FileReader;

import cs3114.gis.core.CommandParser;

import java.io.PrintWriter;

import java.io.BufferedReader;

import java.io.FileNotFoundException;

import java.io.RandomAccessFile;

import java.util.GregorianCalendar;

/**
 *
 */

/**
 * This is the class containing the main method.
 *
 * @author Tianyu Geng
 * @version Nov 17, 2012
 */
public class Control {
    private static RandomAccessFile database;
    private static BufferedReader scripts;
    private static PrintWriter log;

    /**
     * The main function
     *
     * @param args
     */
    public static void main(String[] args) {
        database = null;
        scripts = null;
        log = null;
        if (args.length < 3) {
            System.out
                    .println("Usage: java GIS <database file name> <command script file name> <log file name>");
            System.exit(0);
        }

        CommandParser cmdPar;
        // create the RandomAccessFile for the database
        try {
            File dbFile = new File(args[0]);
            dbFile.delete();
            dbFile.createNewFile();
            database = new RandomAccessFile(dbFile, "rw");

        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot read and write " + args[0]);
            System.exit(1);

        }
        catch (IOException e) {
            System.err.println("Cannot create database.");
            System.exit(1);
        }
        // create the reader for the script file
        try {
            scripts = new BufferedReader(new FileReader(args[1]));
        }
        catch (FileNotFoundException e) {
            System.err
                    .println("Cannot read the script file "
                            + args[1]
                            + ". \nPlease make sure you have the permission to read the script file.");
            System.exit(1);
        }
        // create the PrintWriter for the log file
        try {
            File logFile = new File(args[2]);
            logFile.delete();
            logFile.createNewFile();
            log = new PrintWriter(logFile, "UTF-8");

        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot write to " + args[2]);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Cannot create the log file.");
        }
        // Create a command parser
        cmdPar = new CommandParser(scripts, database, log);
        log.println("GIS Program - Tianyu Geng (tony1)");
        log.println("Database  : " + args[0]);
        log.println("Script    : " + args[1]);
        log.println("Log       : " + args[2]);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("EEE, d MMM yyyy HH:mm:ss");
        log.println(("Start time: " + sdf.format(new GregorianCalendar()
                .getTime())));
        log.println();
        cmdPar.parseAndExecute();

        exit();

    }

    /**
     * The function that will close all the IO objects and exit the program.
     */
    public static void exit() {
        // close the file
        try {
            database.close();
            scripts.close();
        }
        catch (IOException e) {
            System.err.println("ERROR: fail to write to database.");
        }
        // log the date and time
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("EEE, d MMM yyyy HH:mm:ss");
        log.println(("End time: " + sdf.format(new GregorianCalendar()
                .getTime())));
        // close the log file
        log.close();

        System.exit(0);
    }

}
