package comp128.gestureRush;

import java.util.ArrayList;
import java.util.List;

import edu.macalester.graphics.Point;

public class GestureTemplate {
    private final String name;
    private final ArrayList<Point> points;

    public GestureTemplate(String name, ArrayList<Point> points) {
        this.name = name;
        this.points = points;
    }
    public String getName() { 
        return name; 
    }
    public List<Point> getPoints() { 
        return points; 
    }

    public void removePoint(){
        
    }



}

