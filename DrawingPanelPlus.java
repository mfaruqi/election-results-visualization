/*************************************************************************
 *  DrawingPanelPlus.java
 *************************************************************************/

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;

/**
 *  <i>DrawingPanelPlus</i>. This class provides a basic capability for
 *  creating drawings with your programs. 
 *  It is a mash up of the PurpleAmerica Draw.java and 
 *  University of Washington's DrawingPanel. 
 *  <p>
 *  For additional documentation, see <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */


public class DrawingPanelPlus 
{

   // boundary of drawing canvas, 0% border
   private static final double BORDER = 0.0;
   private static final double DEFAULT_XMIN = 0.0;
   private static final double DEFAULT_XMAX = 1.0;
   private static final double DEFAULT_YMIN = 0.0;
   private static final double DEFAULT_YMAX = 1.0;

   /** default canvas size is SIZE-by-SIZE */
   private static final int DEFAULT_SIZE = 512;

   /** default pen radius */
   private static final double DEFAULT_PEN_RADIUS = 0.002;

   /** default font */
   private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

   /** canvas width */
   private int width  = DEFAULT_SIZE;
   /** canvas height */
   private int height = DEFAULT_SIZE;

   /** default pen color */
   private static final Color DEFAULT_PEN_COLOR   = Color.BLACK;
   /** default clear or background color */
   private static final Color DEFAULT_CLEAR_COLOR = Color.WHITE;

   /** current pen color */
   private Color penColor;

   /** current pen radius */
   private double penRadius;

   /** show we draw immediately or wait until next show? */
   private boolean defer = false;

   /** The minimum x value of the window */
   private double xmin;
   /** The maximum x value of the window */
   private double xmax;
   /** The minimum y value of the window */
   private double ymin;
   /** The maximum y value of the window */
   private double ymax;

   /** current font */
   private Font font;

   /** Attach this to the UW drawing panel so we can use this library with GradeIt */
   DrawingPanel panel = null;
   
   /**
    * Create an empty drawing object.
    */
   public DrawingPanelPlus() 
   {
      init(1,1);
   }

   /**
    * Create an empty drawing object.
    */
   public DrawingPanelPlus(int w, int h) 
   {
      init(Math.max(1, w),Math.max(1, h));
   }

   /** Initialize the window by setting up the DrawingPanel, setting the X and Y scaling and other defaults.
    * @param width the width of the window
    * @param height the height of the window 
    */
   private void init(int width, int height) 
   {
      if ( panel == null ) 
         panel = new DrawingPanel(width, height);
      else 
         panel.setSize(width, height);
      this.width = width;
      this.height = height;
      // if width and height are 1, then don't show yet. 
      panel.setVisible(!( width == 1 && height == 1));      
      setXscale();
      setYscale();
      setPenColor();
      setPenRadius();
      setFont();
      clear();
   }

    /**
     * Set the window size to w-by-h pixels.
     *
     * @param w the width as a number of pixels
     * @param h the height as a number of pixels
     * @throws a RunTimeException if the width or height is 0 or negative
     */
   public void setCanvasSize(int w, int h) {
      if (w < 1 || h < 1) throw new RuntimeException("width and height must be positive");
      init(w,h);
   }


  /*************************************************************************
   *  User and screen coordinate systems
   *************************************************************************/

   /**
    * Set the x-scale to be the default (between 0.0 and 1.0).
    */
   public void setXscale() { setXscale(DEFAULT_XMIN, DEFAULT_XMAX); }

   /**
    * Set the y-scale to be the default (between 0.0 and 1.0).
    */
   public void setYscale() { setYscale(DEFAULT_YMIN, DEFAULT_YMAX); }

   /**
    * Set the x-scale (a 10% border is added to the values)
    * @param min the minimum value of the x-scale
    * @param max the maximum value of the x-scale
    */
   public void setXscale(double min, double max) 
   {
      double size = max - min;
      xmin = min - BORDER * size;
      xmax = max + BORDER * size;
   }

   /**
    * Set the y-scale (a 10% border is added to the values).
    * @param min the minimum value of the y-scale
    * @param max the maximum value of the y-scale
    */
   public void setYscale(double min, double max) 
   {
      double size = max - min;
      ymin = min - BORDER * size;
      ymax = max + BORDER * size;
   }

   // helper functions that scale from user coordinates to screen coordinates and back
   private double  scaleX(double x) { 
      return width  * (x - xmin) / (xmax - xmin); }
   private double  scaleY(double y) { 
      return height * (ymax - y) / (ymax - ymin); }
   private double factorX(double w) { 
      return w * width  / Math.abs(xmax - xmin);  }
   private double factorY(double h) { 
      return h * height / Math.abs(ymax - ymin);  }
   private double   userX(double x) { 
      return xmin + x * (xmax - xmin) / width;    }
   private double   userY(double y) { 
      return ymax - y * (ymax - ymin) / height;   }


   /**
    * Clear the screen to the default color (white).
    */
   public void clear() { clear(DEFAULT_CLEAR_COLOR); }
   /**
    * Clear the screen to the given color.
    * @param color the Color to make the background
    */
   public void clear(Color color) 
   {
      Graphics g = panel.getGraphics();
      g.setColor(color);
      g.fillRect(0, 0, width, height);
      g.setColor(penColor);
   }

   /**
    * Get the current pen radius.
    */
   public double getPenRadius() { 
      return penRadius; }

   /**
    * Set the pen size to the default (.002).
    */
   public void setPenRadius() { setPenRadius(DEFAULT_PEN_RADIUS); }

   /**
    * Set the radius of the pen to the given size.
    * @param r the radius of the pen
    * @throws RuntimeException if r is negative
    */
   public void setPenRadius(double r) 
   {
      if (r < 0) throw new RuntimeException("pen radius must be positive");
      penRadius = r * DEFAULT_SIZE;
      BasicStroke stroke = new BasicStroke((float) penRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
      // BasicStroke stroke = new BasicStroke((float) penRadius);
      panel.getGraphics().setStroke(stroke);
   }

   /**
    * Get the current pen color.
    */
   public Color getPenColor() { return penColor; }

   /**
    * Set the pen color to the default color (black).
    */
   public void setPenColor() { setPenColor(DEFAULT_PEN_COLOR); }

   /**
    * Set the pen color to the given color.
    * @param color the Color to make the pen
    */
   public void setPenColor(Color color) {
      penColor = color;
      panel.getGraphics().setColor(penColor);
   }

   /**
    * Set the pen color to the given RGB color.
    * @param red the amount of red (between 0 and 255)
    * @param green the amount of green (between 0 and 255)
    * @param blue the amount of blue (between 0 and 255)
    * @throws IllegalArgumentException if the amount of red, green, or blue are outside prescribed range
    */
   public void setPenColor(int red, int green, int blue) {
      if (red   < 0 || red   >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
      if (green < 0 || green >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
      if (blue  < 0 || blue  >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
      setPenColor(new Color(red, green, blue));
   }


   public void xorOn()   { panel.getGraphics().setXORMode(DEFAULT_CLEAR_COLOR); }
   public void xorOff()  { panel.getGraphics().setPaintMode();         }

   /**
    * Get the current font.
    */
   public Font getFont() { 
      return font; }

   /**
    * Set the font to the default font (sans serif, 16 point).
    */
   public void setFont() { setFont(DEFAULT_FONT); }

   /**
    * Set the font to the given value.
    * @param f the font to make text
    */
   public void setFont(Font f) { font = f; }


  /*************************************************************************
   *  Drawing geometric shapes.
   *************************************************************************/

   /**
    * Draw a line from (x0, y0) to (x1, y1).
    * @param x0 the x-coordinate of the starting point
    * @param y0 the y-coordinate of the starting point
    * @param x1 the x-coordinate of the destination point
    * @param y1 the y-coordinate of the destination point
    */
   public void line(double x0, double y0, double x1, double y1) 
   {
      panel.getGraphics().draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
   
   }

   /**
    * Draw one pixel at (x, y).
    * @param x the x-coordinate of the pixel
    * @param y the y-coordinate of the pixel
    */
   private void pixel(double x, double y) 
   {
      panel.getGraphics().fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
   }

   /**
    * Draw a point at (x, y).
    * @param x the x-coordinate of the point
    * @param y the y-coordinate of the point
    */
   public void point(double x, double y) 
   {
      double xs = scaleX(x);
      double ys = scaleY(y);
      double r = penRadius;
      // double ws = factorX(2*r);
      // double hs = factorY(2*r);
      // if (ws <= 1 && hs <= 1) pixel(x, y);
      if (r <= 1) pixel(x, y);
      else panel.getGraphics().fill(new Ellipse2D.Double(xs - r/2, ys - r/2, r, r));
   }

   /**
    * Draw a circle of radius r, centered on (x, y).
    * @param x the x-coordinate of the center of the circle
    * @param y the y-coordinate of the center of the circle
    * @param r the radius of the circle
    * @throws RuntimeException if the radius of the circle is negative
    */
   public void circle(double x, double y, double r) 
   {
      if (r < 0) throw new RuntimeException("circle radius can't be negative");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*r);
      double hs = factorY(2*r);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
   }

   /**
    * Draw filled circle of radius r, centered on (x, y).
    * @param x the x-coordinate of the center of the circle
    * @param y the y-coordinate of the center of the circle
    * @param r the radius of the circle
    * @throws RuntimeException if the radius of the circle is negative
    */
   public void filledCircle(double x, double y, double r) 
   {
      if (r < 0) throw new RuntimeException("circle radius can't be negative");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*r);
      double hs = factorY(2*r);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
   }


   /**
    * Draw an ellipse with given semimajor and semiminor axes, centered on (x, y).
    * @param x the x-coordinate of the center of the ellipse
    * @param y the y-coordinate of the center of the ellipse
    * @param semiMajorAxis is the semimajor axis of the ellipse
    * @param semiMinorAxis is the semiminor axis of the ellipse
    * @throws RuntimeException if either of the axes are negative
    */
   public void ellipse(double x, double y, double semiMajorAxis, double semiMinorAxis) 
   {
      if (semiMajorAxis < 0) throw new RuntimeException("ellipse semimajor axis can't be negative");
      if (semiMinorAxis < 0) throw new RuntimeException("ellipse semiminor axis can't be negative");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*semiMajorAxis);
      double hs = factorY(2*semiMinorAxis);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
   }

   /**
    * Draw an ellipse with given semimajor and semiminor axes, centered on (x, y).
    * @param x the x-coordinate of the center of the ellipse
    * @param y the y-coordinate of the center of the ellipse
    * @param semiMajorAxis is the semimajor axis of the ellipse
    * @param semiMinorAxis is the semiminor axis of the ellipse
    * @throws RuntimeException if either of the axes are negative
    */
   public void filledEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) 
   {
      if (semiMajorAxis < 0) throw new RuntimeException("ellipse semimajor axis can't be negative");
      if (semiMinorAxis < 0) throw new RuntimeException("ellipse semiminor axis can't be negative");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*semiMajorAxis);
      double hs = factorY(2*semiMinorAxis);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
   }

   /**
    * Draw an arc of radius r, centered on (x, y), from angle1 to angle2 (in degrees).
    * @param x the x-coordinate of the center of the circle
    * @param y the y-coordinate of the center of the circle
    * @param r the radius of the circle
    * @param angle1 the starting angle. 0 would mean an arc beginning at 3 o'clock.
    * @param angle2 the angle at the end of the arc. For example, if
    *        you want a 90 degree arc, then angle2 should be angle1 + 90.
    * @throws RuntimeException if the radius of the circle is negative
    */
   public void arc(double x, double y, double r, double angle1, double angle2) 
   {
      if (r < 0) throw new RuntimeException("arc radius can't be negative");
      while (angle2 < angle1) angle2 += 360;
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*r);
      double hs = factorY(2*r);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().draw(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
   }

   /**
    * Draw a square of side length 2r, centered on (x, y).
    * @param x the x-coordinate of the center of the square
    * @param y the y-coordinate of the center of the square
    * @param r radius is half the length of any side of the square
    * @throws RuntimeException if r is negative
    */
   public void square(double x, double y, double r) 
   {
      if (r < 0) throw new RuntimeException("square side length can't be negative");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*r);
      double hs = factorY(2*r);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
   }

   /**
    * Draw a filled square of side length 2r, centered on (x, y).
    * @param x the x-coordinate of the center of the square
    * @param y the y-coordinate of the center of the square
    * @param r radius is half the length of any side of the square
    * @throws RuntimeException if r is negative
    */
   public void filledSquare(double x, double y, double r) 
   {
      if (r < 0) throw new RuntimeException("square side length can't be negative");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*r);
      double hs = factorY(2*r);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
   }


   /**
    * Draw a rectangle of given half width and half height, centered on (x, y).
    * @param x the x-coordinate of the center of the rectangle
    * @param y the y-coordinate of the center of the rectangle
    * @param halfWidth is half the width of the rectangle
    * @param halfHeight is half the height of the rectangle
    * @throws RuntimeException if halfWidth or halfHeight is negative
    */
   public void rectangle(double x, double y, double halfWidth, double halfHeight) 
   {
      if (halfWidth  < 0) throw new RuntimeException("half width can't be negative");
      if (halfHeight < 0) throw new RuntimeException("half height can't be negative");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*halfWidth);
      double hs = factorY(2*halfHeight);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
   }

   /**
    * Draw a filled rectangle of given half width and half height, centered on (x, y).
    * @param x the x-coordinate of the center of the rectangle
    * @param y the y-coordinate of the center of the rectangle
    * @param halfWidth is half the width of the rectangle
    * @param halfHeight is half the height of the rectangle
    * @throws RuntimeException if halfWidth or halfHeight is negative
    */
   public void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
      if (halfWidth  < 0) throw new RuntimeException("half width can't be negative");
      if (halfHeight < 0) throw new RuntimeException("half height can't be negative");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(2*halfWidth);
      double hs = factorY(2*halfHeight);
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else panel.getGraphics().fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
   }

   /**
    * Draw a polygon with the given (x[i], y[i]) coordinates.
    * @param x an array of all the x-coordindates of the polygon
    * @param y an array of all the y-coordindates of the polygon
    */
   public void polygon(double[] x, double[] y) {
      int N = x.length;
      GeneralPath path = new GeneralPath();
      path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
      for (int i = 0; i < N; i++)
         path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
      path.closePath();
      panel.getGraphics().draw(path);
   }

   /**
    * Draw a filled polygon with the given (x[i], y[i]) coordinates.
    * @param x an array of all the x-coordindates of the polygon
    * @param y an array of all the y-coordindates of the polygon
    */
   public void filledPolygon(double[] x, double[] y) {
      int N = x.length;
      GeneralPath path = new GeneralPath();
      path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
      for (int i = 0; i < N; i++)
         path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
      path.closePath();
      panel.getGraphics().fill(path);
   }


  /**
    * Draw a polygon outline with the coordinates stored in a Java Polygon object.
    * @param the Java Polygon object
    */
   public void polygon(Polygon2D shape) {
      if (shape==null) throw new RuntimeException("you must pass in a valid Polygon object");
      if (shape.getNumPoints() < 3 ) throw new RuntimeException("your polygon must have at least three points");
      polygon(shape.getXPoints(), shape.getYPoints());
   }


   /**
    * Draw a filled polygon with the coordinates stored in a Java Polygon object.
    * @param the Java Polygon object
    */
   public void filledPolygon(Polygon2D shape) {
      if (shape==null) throw new RuntimeException("you must pass in a valid Polygon object");
      if (shape.getNumPoints() < 3 ) throw new RuntimeException("your polygon must have at least three points");
      filledPolygon(shape.getXPoints(), shape.getYPoints());
   }


  /*************************************************************************
   *  Drawing images.
   *************************************************************************/

   // get an image from the given filename
   private Image getImage(String filename) {
   
      // to read from file
      ImageIcon icon = new ImageIcon(filename);
   
      // try to read from URL
      if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
         try {
            URL url = new URL(filename);
            icon = new ImageIcon(url);
         } 
         catch (Exception e) { /* not a url */ }
      }
   
      // in case file is inside a .jar
      if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
         URL url = DrawingPanelPlus.class.getResource(filename);
         if (url == null) throw new RuntimeException("image " + filename + " not found");
         icon = new ImageIcon(url);
      }
   
      return icon.getImage();
   }

   /**
    * Draw picture (gif, jpg, or png) centered on (x, y).
    * @param x the center x-coordinate of the image
    * @param y the center y-coordinate of the image
    * @param s the name of the image/picture, e.g., "ball.gif"
    * @throws RuntimeException if the image is corrupt
    */
   public void picture(double x, double y, String s) {
      Image image = getImage(s);
      double xs = scaleX(x);
      double ys = scaleY(y);
      int ws = image.getWidth(null);
      int hs = image.getHeight(null);
      if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");
   
      panel.getGraphics().drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
   }

   /**
    * Draw picture (gif, jpg, or png) centered on (x, y),
    * rotated given number of degrees
    * @param x the center x-coordinate of the image
    * @param y the center y-coordinate of the image
    * @param s the name of the image/picture, e.g., "ball.gif"
    * @param degrees is the number of degrees to rotate counterclockwise
    * @throws RuntimeException if the image is corrupt
    */
   public void picture(double x, double y, String s, double degrees) {
      Image image = getImage(s);
      double xs = scaleX(x);
      double ys = scaleY(y);
      int ws = image.getWidth(null);
      int hs = image.getHeight(null);
      if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");
   
      Graphics2D g = panel.getGraphics();
      g.rotate(Math.toRadians(-degrees), xs, ys);
      g.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
      g.rotate(Math.toRadians(+degrees), xs, ys);
   }

   /**
    * Draw picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
    * @param x the center x coordinate of the image
    * @param y the center y coordinate of the image
    * @param s the name of the image/picture, e.g., "ball.gif"
    * @param w the width of the image
    * @param h the height of the image
    * @throws RuntimeException if the image is corrupt
    */
   public void picture(double x, double y, String s, double w, double h) {
      Image image = getImage(s);
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(w);
      double hs = factorY(h);
      if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");
      if (ws <= 1 && hs <= 1) pixel(x, y);
      else {
         panel.getGraphics().drawImage(image, (int) Math.round(xs - ws/2.0),
                                   (int) Math.round(ys - hs/2.0),
                                   (int) Math.round(ws),
                                   (int) Math.round(hs), null);
      }
   }


   /**
    * Draw picture (gif, jpg, or png) centered on (x, y), rotated
    * given number of degrees, rescaled to w-by-h.
    * @param x the center x-coordinate of the image
    * @param y the center y-coordinate of the image
    * @param s the name of the image/picture, e.g., "ball.gif"
    * @param w the width of the image
    * @param h the height of the image
    * @param degrees is the number of degrees to rotate counterclockwise
    * @throws RuntimeException if the image is corrupt
    */
   public void picture(double x, double y, String s, double w, double h, double degrees) {
      Image image = getImage(s);
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(w);
      double hs = factorY(h);
      if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");
      if (ws <= 1 && hs <= 1) pixel(x, y);
      
      Graphics2D g = panel.getGraphics();
      g.rotate(Math.toRadians(-degrees), xs, ys);
      g.drawImage(image, (int) Math.round(xs - ws/2.0),
                                (int) Math.round(ys - hs/2.0),
                                (int) Math.round(ws),
                                (int) Math.round(hs), null);
      g.rotate(Math.toRadians(+degrees), xs, ys);
   }


  /*************************************************************************
   *  Drawing text.
   *************************************************************************/

   /**
    * Write the given text string in the current font, centered on (x, y).
    * @param x the center x-coordinate of the text
    * @param y the center y-coordinate of the text
    * @param s the text
    */
   public void text(double x, double y, String s) {
      panel.getGraphics().setFont(font);
      FontMetrics metrics = panel.getGraphics().getFontMetrics();
      double xs = scaleX(x);
      double ys = scaleY(y);
      int ws = metrics.stringWidth(s);
      int hs = metrics.getDescent();
      panel.getGraphics().drawString(s, (float) (xs - ws/2.0), (float) (ys + hs));
   }

   /**
    * Write the given text string in the current font, centered on (x, y) and
    * rotated by the specified number of degrees
    * @param x the center x-coordinate of the text
    * @param y the center y-coordinate of the text
    * @param s the text
    * @param degrees is the number of degrees to rotate counterclockwise
    */
   public void text(double x, double y, String s, double degrees) {
      double xs = scaleX(x);
      double ys = scaleY(y);
      panel.getGraphics().rotate(Math.toRadians(-degrees), xs, ys);
      text(x, y, s);
      panel.getGraphics().rotate(Math.toRadians(+degrees), xs, ys);
   }

   /**
    * Write the given text string in the current font, left-aligned at (x, y).
    * @param x the x-coordinate of the text
    * @param y the y-coordinate of the text
    * @param s the text
    */
   public void textLeft(double x, double y, String s) {
      Graphics2D g = panel.getGraphics();
      g.setFont(font);
      FontMetrics metrics = g.getFontMetrics();
      double xs = scaleX(x);
      double ys = scaleY(y);
      // int ws = metrics.stringWidth(s);
      int hs = metrics.getDescent();
      g.drawString(s, (float) (xs), (float) (ys + hs));
   }

 }


//Copyright  2000 2011, Robert Sedgewick and Kevin Wayne. 
//Last updated: Tue Dec 30 19:34:02 EST 2014.
//Copied and modified by Lauren Bricker 10/31/2015.