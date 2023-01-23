import java.util.*;

/** 
 * Helper class to store the election data from a file: FileData is an abstraction that contains 
 * the information from the election data files. 
 * Files are expected to be in the format
 * Election year and election type, name1, name2, name 3
 * region1,count11,count12,count13
 * region2,count21,count22,count23
 * ...
 */
public class FileData
{
   /** The identifier: what election is this from? */
   private String identifier; 
   
   /** The (up to) three names from the election. Typcially listed in the order 
    *  Republican, Democrat, Other
    */
   private String[] options; 
   
   /**
    *  each line of the data is stored in a map, the name is maped to an array of 
    *  the three values corresponding to the three names listed on the first line
    *  of the file
    */
   private Map<String, int[]> data = new HashMap<String, int[]>();
   
   /**
    * an assumption that there are only ever a max of three values for each region
    */
   public static final int MAXVALUES = 3; 
  
   /** 
    * Constructor that will read in the file using a scanner
    * Precondition: that the file is in the right format
    * @param dataScan A scanner on the file containing the data
    * @throws IllegalArgumentException if the scanner passed in is null. 
    */
   public FileData(Scanner dataScan)
   {
      if ( dataScan == null) throw new IllegalArgumentException("Scanner must be valid");
      
      String line = dataScan.nextLine();
      Scanner lineScan = new Scanner(line);
      lineScan.useDelimiter(",");
      
      identifier = lineScan.next();
      
      options = new String[MAXVALUES];
      for (int ii = 0; ii < options.length; ii++)
      {
         if (lineScan.hasNext())
            options[ii] = lineScan.next();
      }
   
      while (dataScan.hasNextLine())
      {
         line = dataScan.nextLine();
         lineScan = new Scanner(line); 
         lineScan.useDelimiter(","); 
         String region = lineScan.next();
         int[] values = new int[MAXVALUES]; 
         for (int ii = 0; ii < values.length; ii++)
         {
            if (lineScan.hasNext())
               values[ii] = lineScan.nextInt();
         }
         data.put(region.toLowerCase(), values);
      }
   }
   
   /**
    *  Gets the name of the option at the given index. In other words
    *  index 0 would give you the first name on the first line
    *  @param index The 0 based index of the names in the first line
    *  @return The name of the candidate at the given index. 
    *  @throws IllegalArgumentException if index is less than 0 or 
    *     index greater than the maximum number of values assumed. 
    */
   public String getOption(int index)
   {
      if ( index < 0 || index >= options.length )
         throw new IllegalArgumentException("Region not found");
      return options[index];
   
   }
   
   /** 
    * The identifier of this data file. This is the first section
    * on the first line of the file, typically 
    *  Year Election name
    * @return the identifier from this data file
    */
   public String getIdentifier()
   {
      return identifier;
   }
   
   /** 
    *  Find number of votes of a candidate at a specific 0 based index 
    *  in other words: find the region (county or state), then 
    *  find the number of votes for that candidate in that region. 
    *  @param region The region to find
    *  @param index the index of the candidate to find the vote count for
    *  @return the number of votes for that candiate in that region 
    */
   public int lookup(String region, int index)
   {
      int[] values = data.get(region.toLowerCase()); 
      if ( values == null )
         throw new IllegalArgumentException("Region not found");
      if ( index < 0 || index >= values.length )
         throw new IllegalArgumentException("Region not found");
         
      return values[index];
   }
   
}