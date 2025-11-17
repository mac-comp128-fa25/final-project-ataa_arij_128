package comp128.gestureRush;

import edu.macalester.graphics.Point;

public class FallingGestures {

    double x;
    double y;
    double speed;
    GestureTemplate i;
    Point centroid;

    public FallingGestures(GestureTemplate i, double x, double y, double speed){
        
        this.i = i;
        centroid = getCentroid(i);
        this.x = x;
        this.y = y;
        this.speed = speed;

    }

    private Point getCentroid(GestureTemplate i2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCentroid'");
    }

    
}
