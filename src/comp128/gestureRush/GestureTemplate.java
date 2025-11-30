package comp128.gestureRush;

import java.util.ArrayList;
import java.util.List;

import edu.macalester.graphics.Point;

public class GestureTemplate {
    private final String NAME;
    private final ArrayList<Point> POINTS;

    public GestureTemplate(String name, ArrayList<Point> points) {
        this.NAME = name;
        this.POINTS = points;
    }
    public String getNAME() { 
        return NAME; 
    }
    public List<Point> getPOINTS() { 
        return POINTS; 
    }

    // public void removePoint(){
        // We can delete this right
    // }
    
    public boolean checkifTrue(Point p){
        return false;
    }


}

