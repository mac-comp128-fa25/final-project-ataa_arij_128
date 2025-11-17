package comp128.gestureRush;

import edu.macalester.graphics.Point;
import java.util.ArrayList;
import java.util.List;

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
        return;
    }

    public Point createArrow(){
        return null;
        
    }

    public Point createCircle(){
        return null;
    }

    public Point createTriangle(){
        return null;
    }



}

