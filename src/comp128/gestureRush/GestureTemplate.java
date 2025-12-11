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
    
    /**
     * name getter
     * @return NAME
     */
    public String getName() { 
        return NAME; 
    }
    
    /**
     * this returns points that make up gesture
     * @return
     */
    public List<Point> getPoints() { 
        return POINTS; 
    }
    
}

