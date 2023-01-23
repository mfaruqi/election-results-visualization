/**
 * Mahad Faruqi
 * AP Computer Science A
 * 11/13/20
 */
import java.util.*; // for scanner use
import java.io.*; // for file use
import java.awt.*; // for graphics
/**
 * This program draws a visual representation of data from a given file.
 * It draws the USA or its individual states according to election data in a year
 */
public class Purple {
    /**
     * This class constant scales the drawing panel to how big or small you want
     */
    public static final int SCALE = 30;
    
    /**
     * This class constant is for the directory of the files
     */
    public static final String STRING = "./data/";

    /**
     * The main method asks the user for valid states and valid years. These states and years are used later on in the program a lot.
     * This method calls the printMap method which itself calls other methods.
     * This method also continues the program if the user wants to draw more states. If not it exits. 
     * @param args The arguments to this program
     * @throws FileNotFoundException if file is not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("This program draws a visualization of\ndata from a given file. Enter\n- the 2 letter abbreviation for each state\n- USA for all of the US by state, or\n- USA-county for all of the US by county.\n");

        boolean drawMap = true;
        while (drawMap) {
            Scanner console = new Scanner(System.in);
            String fileName = MyUtils.getFile(STRING, ".txt", console, "What state would you like to see? ");
            String year = "";
            if (fileName.equals("USA-COUNTY")) {
                fileName = "USA-county";
                String tempFileName = "USA";
                year = MyUtils.getFileYear(STRING, ".txt", tempFileName, console, "For what year (1960 to 2012) do you want to see data: ");

            } else {

                year = MyUtils.getFileYear(STRING, ".txt", fileName, console, "For what year (1960 to 2012) do you want to see data: ");
            }



            printMap(fileName, year);


            System.out.print("Do you want to draw something else? ");
            String option = console.next();
            // makes a new drawing panel or program stops
            if (option.toLowerCase().startsWith("y")) {
                System.out.println();
                drawMap = true;

            } else {
                drawMap = false;  
            }
        }
    }

    /**
     * This method is what draws the actual panel.
     * This takes in the valid file name and year and uses them to get the coordinates of the states and draw accordingly.
     * With the help of other methods this also determines the color of each state.
     * @param fileName This file is the valid name the user gave from the main
     * @param year This is the valid year for the file name the user gives above
     * @throws FileNotFoundException if file is not found
     */
    public static void printMap(String fileName, String year) throws FileNotFoundException {
        //colorStates(read);

        // the main file used
        File f = new File(STRING + fileName + ".txt");
        Scanner in = new Scanner(f);
        File file = new File(STRING + "USA" + ".txt");
        File file1 = new File(STRING + "USA" + ".txt");
        Scanner input = new Scanner(file);

        // skips the first 4 lines because they are not necassary for the use of the second file
        input.nextLine();
        input.nextLine();
        input.nextLine();
        input.nextLine();

        double x = 0;
        double y = 0;

        // this is getting the first four numbers for the bounding coordinates
        double x1 = in.nextDouble(), y1 = in.nextDouble(); in.nextLine();
        double x2 = in.nextDouble(), y2 = in.nextDouble(); in.nextLine();

        // int conversion to scale
        // scales the drawing panel accordingly for both red blue and purple
        DrawingPanelPlus purple = new DrawingPanelPlus((int) Math.abs((x2 - x1) * SCALE), (int) Math.abs((y2 - y1) * SCALE));
        DrawingPanelPlus redBlue = new DrawingPanelPlus((int) Math.abs((x2 - x1) * SCALE), (int) Math.abs((y2 - y1) * SCALE));
        //Graphics2D g = window.getGraphics();
        //window.setCanvasSize((int)((x1)-(x2))*SCALE, (int)((y1)-(y2))*-SCALE);

        // sets the scale with the bounding coordinates of the reagion
        purple.setXscale(x1, x2);
        redBlue.setXscale(x1, x2);
        purple.setYscale(y1, y2);
        redBlue.setYscale(y1, y2);


        String stateOrDistrict = "";
        int districts = in.nextInt();
        String stateInitials = "";
        getHeader(fileName, year);

        // prints a different type of statistics if it is just the USA so that states do not repeat
        if (fileName.equals("USA")) {
            displayData(fileName, year, stateInitials, stateOrDistrict);
        }
        
        while (in.hasNext()) {
            stateOrDistrict = in.nextLine();
            stateInitials = in.nextLine();
            stateOrDistrict = stateOrDistrict.trim();
            stateInitials = stateInitials.trim();
            stateOrDistrict = stateOrDistrict.toUpperCase();
            stateInitials = stateInitials.toUpperCase();

            if (in.hasNextInt()) {
                districts = in.nextInt();
            }

            Polygon2D pd = new Polygon2D(districts);
            boolean red = true;
            while (in.hasNextDouble()) {
                x = in.nextDouble();
                y = in.nextDouble();

                pd.addPoint(x, y);
            }

            // draws the region onlyh if the number of points is greater than 3 because or else it would cause an error
            if (pd.getNumPoints() > 3) {

                purple.setPenColor(getColorPurple(fileName, year, stateInitials, stateOrDistrict));
                redBlue.setPenColor(getColorRedBlue(fileName, year, stateInitials, stateOrDistrict));
                redBlue.filledPolygon(pd);
                purple.filledPolygon(pd);

                // again does a differnt type of printing for not "USA"
                if (!fileName.equals("USA")) {
                    displayData(fileName, year, stateInitials, stateOrDistrict);
                }
                // window.text(x, y, state);
                purple.setPenColor(Color.black);
                redBlue.setPenColor(Color.black);
                redBlue.polygon(pd);

                // this is so that the borders dont get in the way of seeing the color mix in the usa-county region               
                if (!fileName.equals("USA-county")) {
                    purple.polygon(pd);


                }
                pd.reset();
            }
        }

        // does a unique border for usa-county so you can see the color mix better
        if (fileName.equals("USA-county")) {
            while (input.hasNext()) {
                purple.polygon(drawUSBorder(input, fileName));

            }
        }
    }

    /**
     * This method draws the exception for the usa-county and just draws a US border to see the color mix better.
     * @param fileName This file is the valid name the user gave from the main
     * @param input This is the second scanner made in the printMap method for the special exception
     * @return This method returns the polygon which is every US state one by one
     * @throws FileNotFoundException if file is not found
     */
    public static Polygon2D drawUSBorder(Scanner input, String fileName) throws FileNotFoundException {

        if (fileName.equals("USA-county")) {

            double xx = 0;
            double yy = 0;
            int d = 0;
            while (input.hasNext()) {
                input.nextLine();
                input.nextLine();

                if (input.hasNextInt()) {
                    d = input.nextInt();
                }
                Polygon2D pf = new Polygon2D(d);
                pf.reset();

                while (input.hasNextDouble()) {
                    xx = input.nextDouble();
                    yy = input.nextDouble();

                    pf.addPoint(xx, yy);
                }


                if (pf.getNumPoints() > 3) {
                    return pf;
                }
            }
        }
        return null;

    }

    /**
     * This method is to get the color of each county or state for the red blue drawing panel.
     * This takes in the correct file and returns the color accordingly for the red and blue drawing panel
     * @param fileName This file is the valid name the user gave from the main
     * @param year This is the valid year for the file name the user gives above
     * @param stateInitials These are used to get into each states file individuallt if the user wants to draw usa-county
     * @param stateOrDistrict This string is used specifically for checking where the state or district is equal, then it checks which vote count is higher. This also makes it possible so you can have multiple regions that are the same all colored the same color
     * @return This method returns the color of either red, blue, or green depending on vote count
     * @throws FileNotFoundException if file is not found
     */
    public static Color getColorRedBlue(String fileName, String year, String stateInitials, String stateOrDistrict) throws FileNotFoundException {
        int redVotes = 0;
        int blueVotes = 0;
        int otherVotes = 0;
        File file1;
        if (fileName.equals("USA-county")) {
            file1 = new File(STRING + stateInitials + year + ".txt");
            //System.out.println(stateInitials);
        } else {
            file1 = new File(STRING + fileName + year + ".txt");
        }
        Scanner read1 = new Scanner(file1);
        read1.nextLine();
        String theState = "";
        while (read1.hasNextLine()) {
            String thisLine = read1.nextLine();
            String[] line = thisLine.split(",");
            redVotes = Integer.parseInt(line[1]);
            blueVotes = Integer.parseInt(line[2]);
            if (line.length > 3) {
                otherVotes = Integer.parseInt(line[3]);

            }
            line[0] = line[0].toUpperCase();
            // goes through the whole file and once the state or district equals a state or districts in this file, it checks the vote and colors the state or district accordingly
            if (line[0].replaceAll("\\s", "").equals(stateOrDistrict.replaceAll("\\s", ""))) {
                if (redVotes > blueVotes && redVotes > otherVotes) {
                    return Color.red;
                } else if (blueVotes > redVotes && blueVotes > otherVotes) {
                    return Color.blue;
                }

                // this means other party has the most votes
                else {
                    return Color.green;
                }
            }
        }
        return Color.red;
    }



    /**
     * This method is to get the color of each county or state for the purple drawing panel. It returns a color based on an equation where votes are used to make an RGB color.
     * @param fileName This file is the valid name the user gave from the main
     * @param year This is the valid year for the file name the user gives above
     * @param stateInitials These are used to get into each states file individuallt if the user wants to draw usa-county
     * @param stateOrDistrict This string is used specifically for checking where the state or district is equal, then it checks which vote count is higher. This also makes it possible so you can have multiple regions that are the same all colored the same color
     * @return This method returns an RGB color based on the votes
     * @throws FileNotFoundException if file is not found
     */
    public static Color getColorPurple(String fileName, String year, String stateInitials, String stateOrDistrict) throws FileNotFoundException {
        int redVotes = 0;
        int blueVotes = 0;
        int otherVotes = 0;
        File file1;
        if (fileName.equals("USA-county")) {
            file1 = new File(STRING + stateInitials + year + ".txt");
            //System.out.println(stateInitials);
        } else {
            file1 = new File(STRING + fileName + year + ".txt");
        }
        Scanner read1 = new Scanner(file1);
        read1.nextLine();
        String theState = "";

        // these are setting a default color because some of the counties dont match perfectly so it just colors them something not too noticible.
        float r1 = (float) 0.6342687;
        float b1 = (float) 0.36573127;
        float g1 = (float) 0.0;

        Color c = new Color(r1, g1, b1);
        while (read1.hasNextLine()) {
            String thisLine = read1.nextLine();
            String[] line = thisLine.split(",");
            redVotes = Integer.parseInt(line[1]);
            blueVotes = Integer.parseInt(line[2]);
            if (line.length > 3) {
                otherVotes = Integer.parseInt(line[3]);
            } else {
                otherVotes = 0;
            }

            float addition = (float) redVotes + blueVotes + otherVotes;

            float r = (float) redVotes / addition;
            float b = (float) blueVotes / addition;
            float g = (float) otherVotes / addition;
            //System.out.println(r + " " + g + " " + b);
            line[0] = line[0].toUpperCase();

            // goes through the whole file and once the state or district equals a state or districts in this file, it checks the vote and colors the state or district accordingly
            if (line[0].replaceAll("\\s", "").equals(stateOrDistrict.replaceAll("\\s", ""))) {
                c = new Color(r, g, b);

            }
        }
        return c;
    }

    /**
     * This method prints out each county or states vote count to the console for the red, blue, and green candidate.
     * @param fileName This file is the valid name the user gave from the main
     * @param year This is the valid year for the file name the user gives above
     * @param stateInitials These are used to get into each states file individually if the user wants to draw usa-county
     * @param stateOrDistrict This string is used specifically for checking where the state or district is equal. This is really important because you cannot just print out the entire voting file. You have to print out each district even if it repeats
     * @throws FileNotFoundException if file is not found
     */
    public static void displayData(String fileName, String year, String stateInitials, String stateOrDistrict) throws FileNotFoundException {
        File data;
        if (fileName.equals("USA-county")) {
            data = new File(STRING + stateInitials + year + ".txt");

        } else {
            data = new File(STRING + fileName + year + ".txt");
        }
        //System.out.println(state + "<<<< HERE");
        Scanner in = new Scanner(data);
        String line = in.nextLine();
        String[] header = line.split(",");
        String head = header[0];
        String repCand = header[1];
        String demCand = header[2];
        String otherCand = "Other";
        if (header.length > 3) {
            otherCand = header[3];
        }
        while (in.hasNext()) {
            //System.out.println("******* " + stateInitials);
            String l = in.nextLine();
            String[] votes = l.split(",");
            String county = votes[0];
            String repVotes = votes[1];
            String demVotes = votes[2];
            String otherVotes = "0";
            if (votes.length > 3) {
                otherVotes = votes[3];
            }
            //System.out.println(votes[0].replaceAll("\\s", "").toUpperCase() + "   " + state.replaceAll("\\s", ""));
            if (fileName.equals("USA")) {
                System.out.println("\nDisplaying " + county + " data");
                System.out.println("  Red   = " + repVotes + "\n  Blue  = " + demVotes + "\n  Green = " + otherVotes + "");
            } else if (votes[0].replaceAll("\\s", "").toUpperCase().equals(stateOrDistrict.replaceAll("\\s", ""))) {
                if (fileName.equals("USA-county")) {
                    System.out.println("\nDisplaying " + county + " (" + stateInitials + ") data");
                } else {
                    System.out.println("\nDisplaying " + county + " data");
                }
                System.out.println("  Red   = " + repVotes + "\n  Blue  = " + demVotes + "\n  Green = " + otherVotes + "");


            }
        }
    }

    /**
     * This method is just to solve a fence post problem and gets which presidential election it is and what colors represent what candidates.
     * Then it prints those out at the top.
     * @param fileName This file is the valid name the user gave from the main
     * @param year This is the valid year for the file name the user gives above
     * @throws FileNotFoundException if file is not found
     */
    public static void getHeader(String fileName, String year) throws FileNotFoundException {
        File data;
        if (fileName.equals("USA-county")) {
            data = new File(STRING + "USA" + year + ".txt");

        } else {
            data = new File(STRING + fileName + year + ".txt");
        }
        ///System.out.println(state + "<<<< HERE");
        Scanner in = new Scanner(data);
        String line = in.nextLine();
        String[] header = line.split(",");
        String head = header[0];
        String repCand = header[1];
        String demCand = header[2];
        String otherCand = "Other";
        if (header.length == 4) {
            otherCand = header[3];
        }

        System.out.println("\nDisplaying " + head + " data");
        System.out.println("  Red   = " + repCand + "\n  Blue  = " + demCand + "\n  Green = " + otherCand + "");

    }
}
