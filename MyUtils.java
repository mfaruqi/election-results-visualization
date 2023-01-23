/**
 * Mahad Faruqi
 * AP Computer Science A
 * 10/24/20
 */

import java.util.*;
import java.io.*;

/**
 * This is a file to help with methods that are used a lot, and I can use them in other programs
 */
public class MyUtils {

    /**
     * This method is robust input for an integer. If the user does not type an integer it tells them to or if its not between the min or max.
     * @param in This is the scanner used to get the input
     * @param prompt This is the string you can use to ask whatever you want
     * @param min This is the lowest number the user can type
     * @param max This is the highest number the user can round to
     * @return Returns only the integer that the user tyoes
     */
    public static int getNumber(Scanner in , String prompt, int min, int max) {

        while (true) {
            System.out.print(prompt);
            if (! in .hasNextInt()) { 
                in.next();
                System.out.println("You must enter an *integer* between " + min + " and " + max + ".");
            } else {
                int num = in .nextInt();
                if (num < min || num > max) {
                    System.out.println("Your number needs to be between " + min + " and " + max + ".");
                } else {
                    return num;
                }
            }
        }
    }

    /** 
     * This method is for rounding.
     * @param number This is the number they would like to round
     * @param roundTo this is what they want to round to (10 is to the tenths place and so on)
     * @return Returns the rounded number
     */
    public static double round(double number, int roundTo) {
        number = Math.round(number * roundTo);
        number /= roundTo;
        return number;
    }

    /**
     * This method is to get a file that exists with one part. This method is used for getting a valid file name.
     * @param directory This is the directory for a file
     * @param ending This is the ending for a file
     * @param in This is the scanner used to get the file name
     * @param prompt This is the prompt that the user sees
     * @return This method returns the valid file name
     */
    public static String getFile(String directory, String ending, Scanner in , String prompt) {

        System.out.print(prompt);
        String fileName = in .next();
        File f = new File(directory + fileName + ending);
        while (!f.exists()) {
            System.out.println("File not found. Try again. ");
            System.out.print(prompt);
            fileName = in .next();
            f = new File(directory + fileName + ending);
        }
        fileName = fileName.toUpperCase();
        return fileName;
    }

    /**
     * This method is to get a file that exists with two parts, first one is from the method getFile. This method is used for getting a valid second part of the file name. 
     * It uses the fileName string to further check if the file exists with the year the user typed.
     * @param directory This is the directory for a file
     * @param ending This is the ending for a file
     * @param fileName this is the file from the last method and is used to test if the file exists with a year
     * @param in This is the scanner used to get the file name
     * @param prompt This is the prompt that the user sees
     * @return This method returns the valid year
     */
    public static String getFileYear(String directory, String ending, String fileName, Scanner in , String prompt) {

        System.out.print(prompt);
        String year = in .next();
        File f = new File(directory + fileName + year + ending);
        while (!f.exists()) {
            System.out.println("File not found. Try again. ");
            System.out.print(prompt);
            year = in .next();
            f = new File(directory + fileName + year + ending);
        }
        return year;
    }

    /**
     * This method makes sure the filname is correct with no ending. 
     * So that the user can type ".txt" or whatever type of document it is.
     * @param directory this is the directory for a file
     * @param in this is the scanner passed in to get user input
     * @param prompt this is the string passed in to showcase what the program wants the user to type
     * @return returns a correct filename
     */
    public static String getFileName(String directory, Scanner in , String prompt) {
        System.out.print(prompt + " ");
        String fileName = in .next();
        File f = new File(directory + fileName);
        while (!f.exists()) {
            System.out.print("File not found. Try again: ");
            fileName = in .next();
            f = new File(directory + fileName);
        }
        return fileName;
    }

    /**
     * This method creates a file and makes sure that you can write to it
     * If you can write to it does it returns or else it asks for a new file
     * @param in this is the scanner passed in to get user input
     * @param prompt this is the string passed in to showcase what the program wants the user to type
     * @param directory this is the directory of the file
     * @param ending this is the ending of the file
     * @return returns a correct filename that can be written to
     * @throws IOException if file cannot exist
     */

    public static String getOutputFileName(Scanner in , String prompt, String directory, String ending) throws IOException {
        System.out.print(prompt + " ");
        String fileName = in .next();
        File f = new File(fileName);
        f.createNewFile();
        //f.setReadOnly();
        while (!f.canWrite()) {
            System.out.print("Bad filename ");
            fileName = in .next();
            f = new File(fileName);
            f.createNewFile();
            //f.setReadOnly();
            //System.out.println(f.setReadOnly());
        }
        return fileName;
    }

    /**
     * This method creates a file and makes sure that you can write to it
     * @param fileName this is the file name the user wants to use
     * @param directory this is the directory of the file
     * @param ending this is the ending of the file
     * @return returns null if you can't write but if you can it returns the file name
     * @throws IOException if file cannot exist
     */
    public static String checkIfCanWrite(String fileName, String directory, String ending) throws IOException {
        File f = new File(directory + fileName + ending);
        f.createNewFile();
        //f.setReadOnly();
        if (!f.canWrite()) {
            System.out.println("Error opening output file for writing. Curve not drawn.");
            return null;
        }

        return directory + fileName + ending;
    }
}
