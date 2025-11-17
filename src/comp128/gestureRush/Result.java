package comp128.gestureRush;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Result {
    public final String name;
    public final double accuracyNum;

    public Result ( String name, double num){
        this.name = name;
        this.accuracyNum = num;
    }
    
}
