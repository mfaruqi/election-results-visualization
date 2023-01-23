// Lauren Bricker
// Testing Purple America assignment, part 1
// Then using it to test the new Polygon2D of the Draw object
// 10/2015

import java.awt.*;

public class DrawSample
{

   public static void main(String[] args)
   {

      DrawingPanelPlus window = new DrawingPanelPlus(500,500);

      window.setCanvasSize(600, 600); 
      window.filledCircle(0.5, 0.5, .3); 
      window.setPenColor(Color.BLUE);
      
      Polygon2D pd = new Polygon2D(5); 
      pd.addPoint(.1, .1); 
      pd.addPoint(.2, .1);
      pd.addPoint(.2, .2);
      pd.addPoint(.1, .2); 
      pd.addPoint(.1, .1);
      window.filledPolygon(pd); 
      
      pd.reset();
      
      window.setPenColor(Color.GREEN);
      
      pd.addPoint(.4, .4); 
      pd.addPoint(.4, .6);
      pd.addPoint(.55, .7);
      pd.addPoint(.7, .6); 
      pd.addPoint(.7, .4);
      pd.addPoint(.4, .4);
      window.polygon(pd); 

      window.setPenColor(Color.RED);
      window.setXscale(100, 200);
      window.setYscale(50, 100);
      pd.reset();
      pd.addPoint(150, 75); 
      pd.addPoint(175, 100); 
      pd.addPoint(160, 50); 
      pd.addPoint(150, 75);
      window.polygon(pd);

   }
}