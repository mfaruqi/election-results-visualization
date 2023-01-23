// Lauren Bricker
// Polygon2d
// Version 1.0 - 10/30/15 - LJB - Created

/**
 * 2 dimensional polygon class that contains doubles
 * because Java did create one in the class library. 
 */

import java.util.*;

public class Polygon2D
{

   /** The number of points in this polygon */
   private int npoints = 0; 
   
   /** The list of x coordinates stored as double values */
   private double[] xpoints = null; 
   
   /** The list of y coordinates stored as double values */
   private double[] ypoints = null; 
   
   /** How many vertices to grow the underlying structure by when the arrays need to grow.  */
   private static final int GROWBY = 10; 


   /** 
    *  Constructor: create an empty polygon 
    */
   public Polygon2D()
   {
   }
   
  /** 
    *  Constructor: create an empty polygon that can hold the given number of vertices
    *  @param nvertices the number of vertices you expect this polygon to hold. 
    */
   public Polygon2D(int nvertices)
   {
      xpoints = new double[nvertices];
      ypoints = new double[nvertices];
   }
     
  /** 
   *  Constructor: create an empty polygon 
   *  @param xpoints an array of X coordinates
   *  @param ypoints an array of Y coordinates
   *  @throws IllegalArgumentException if the lenght of xpoints or the length of ypoints are 0 or they're not equal.
   *  @throws NullPointerException  if xpoints or ypoints is null.
   */
   public Polygon2D(double[] xpoints, double[] ypoints)
   {
      if ( xpoints == null || ypoints == null )
         throw new NullPointerException("arrays can not be null");
      if (xpoints.length == 0 || ypoints.length == 0 || xpoints.length != ypoints.length)
         throw new IllegalArgumentException("arrays must be the same length and contain at least one point");
      npoints = xpoints.length;
      this.xpoints = xpoints; 
      this.ypoints = ypoints; 
   }
  
  /** 
   *  Resets this Polygon object to an empty polygon. 
   *  The coordinate arrays and the data in them are left 
   * untouched but the number of points is reset to zero to mark the 
   *  old vertex data as invalid and to start accumulating 
   * new vertex data at the beginning. All internally-cached data 
   * relating to the old vertices are discarded. Note that since the 
   * coordinate arrays from before the reset are reused, 
   * creating a new empty Polygon might be more memory 
   * efficient than resetting the current one if the number of 
   * vertices in the new polygon data is significantly 
   * smaller than the number of vertices in the data from before the reset.
   */
   
   public void reset()
   {
      npoints = 0; 
   }
   
   /**
    * Appends the specified coordinates to this Polygon.
    * @param x the specified X coordinate
    * @param y the specified Y coordinate   
    */
    
   public void addPoint(double x, double y)
   {
      if ( npoints + 1 > xpoints.length )
      { 
         xpoints = Arrays.copyOf(xpoints, npoints + GROWBY); 
      }
      if ( npoints + 1 > ypoints.length )
      { 
         ypoints = Arrays.copyOf(ypoints, npoints + GROWBY); 
      }
   
      xpoints[npoints] = x; 
      ypoints[npoints] = y; 
      npoints++;
   } 
   
   /**
    * Gets the number of points contained in this polygon
    * @return The number of points in this polygon
    */
    
   public int getNumPoints()
   {
      return npoints;
   }

   
   /**
    * Gets the x coordinates of the polygon, as an array
    * @return an array containing only the x coordinates
    */
    
   public double[] getXPoints()
   {
      return Arrays.copyOf(xpoints, npoints);
   }

   /**
    * Gets the y coordinates of the polygon, as an array
    * @return an array containing only the y coordinates
    */
    
   public double[] getYPoints()
   {
      return Arrays.copyOf(ypoints, npoints);
   }


}
