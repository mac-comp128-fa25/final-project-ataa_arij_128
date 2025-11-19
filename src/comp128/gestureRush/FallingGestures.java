package comp128.gestureRush;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

public class FallingGestures extends GraphicsGroup{

    private double x;
    private double y;
    private final double speed;
    private GestureTemplate gesture;
    private Point centroid;
    private double maxX; //From the canvas, bounds of the gesture falling
    private double maxY;

    public FallingGestures(GestureTemplate gesture, double x, double y, double maxX, double maxY){
        
        this.gesture = gesture;
        centroid = calculateCentroid(gesture); //Figure out why this is overideable
        this.x = x;
        this.y = y;
        this.speed = 120 + Math.random() * 78;
        this.maxX = maxX;
        this.maxY = maxY;

    }

    /**
     * Helper function which calculates the centroid of a given deque of points
     * 
     * @param path
     * @return centroid
     */
    public Point calculateCentroid(GestureTemplate g) {
        double totalX = 0;
        double totalY = 0;
        List<Point> path = g.getPoints(); 
        Iterator<Point> iter = path.iterator();

        while (iter.hasNext()) {
            Point point = iter.next();
            totalX += point.getX();
            totalY += point.getY();
        }

        double averageX = totalX / path.size();
        double averageY = totalY / path.size();
        Point centroid = new Point(averageX, averageY);

        return centroid;
    }

    /**
     * Update the cannon ball's position if it is in bounds
     * @return true if the ball is in within the maxXBound and maxYBound
     */
     /* 
    public boolean updatePosition(double dt) {
        centerX = dt * xVelocity + centerX; 
        centerY = dt * yVelocity + centerY;
        if (centerX > 0 && centerY > 0 && maxX > centerX && maxY > centerY){
           ballShape.setX(centerX); 
           ballShape.setY(centerY);
           yVelocity -= (GRAVITY * dt);
           return true;
        }
        return false;
    }
     */
       // Think about if this moves the entire gesture (if not figure out how to do so)

       public boolean update(double x, double height){
        moveBy(0, this.speed * x);
        return getY() < height;
       }
 


    
}
