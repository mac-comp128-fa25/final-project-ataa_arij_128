package comp128.gestureRush;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;

public class GestureRushGame {

    private final CanvasWindow canvas;
    private FallingGestures currentGesture;
    private final List<GestureTemplate> templates;
    private final Random rand = new Random();

    private double shapeSize = 90;
    private PlayerEraser eraser;
    private double eraserRadius = 5;
    private final ArrayList<Point> removedLog = new ArrayList<>();
    private Timer timer;
    private TimerTask task; // 1 timer for number of gestures falling, another for which gestures falling

    public GestureRushGame() {
        timer = new Timer();
        task = new TimerTask() { // Currently has no effect, after certain amount of time more gestures start falling
            @Override
            public void run(){
                GestureTemplate g = templates.get(rand.nextInt(templates.size()));
                FallingGestures nextGesture = new FallingGestures(g, eraserRadius, shapeSize);
                canvas.add(nextGesture);
            }
        };
        timer.schedule(task, 2000); // Delay will be 15000 (15 seconds) 
        canvas = new CanvasWindow("Gesture Rush", 600, 600);
        canvas.setBackground(Color.WHITE);
        templates = new ArrayList<>();
        templates.add(createArrow());
        templates.add(createCircle());
        templates.add(createTriangle());

        eraser = new PlayerEraser(canvas, (p, r) -> {
            if (currentGesture == null) return 0;
            int removed = currentGesture.eraseAt(p, r, removedLog);
            if (removed > 0 && currentGesture.isFullyErased()) {
                canvas.remove(currentGesture);
                currentGesture = null;
                spawnNext();
            }
            return removed;
        }, eraserRadius);

        spawnNext();
        canvas.animate(this::update);
    }

    private void spawnNext() {
        GestureTemplate t = templates.get(rand.nextInt(templates.size()));
        currentGesture = new FallingGestures(t, canvas.getWidth(), shapeSize);
        canvas.add(currentGesture);
    }

    private void update() {
        double dt = 0.025;
        if (currentGesture != null) {
            boolean stillOn = currentGesture.update(dt, canvas.getHeight());
            if (!stillOn) {
                canvas.remove(currentGesture);
                currentGesture = null;
                spawnNext();
            }
        }
    }
    
    private void addLinePoints(List<Point> lineseg,double x1, double y1, double x2, double y2,int steps){
        for (int i = 0; i <= steps; i++) {
        double t = i / (double) steps;
        double x = x1 + t * (x2 - x1);
        double y = y1 + t * (y2 - y1);
        lineseg.add(new Point(x, y));
    }

    }
                            

    private GestureTemplate createArrow() {
    ArrayList<Point> arrowPoints = new ArrayList<>();
    addLinePoints(arrowPoints, 50, 10, 50, 70, 24); 
    addLinePoints(arrowPoints, 50, 10, 30, 30, 16);
    addLinePoints(arrowPoints, 50, 10, 70, 30, 16);  
    return new GestureTemplate("arrow", arrowPoints);
}


    private GestureTemplate createCircle() {
        ArrayList<Point> circlePoints = new ArrayList<>();
        double cx = 50, cy = 50, r = 35;
        int steps = 32;
        for (int i = 0; i <= steps; i++) {
            double a = 2 * Math.PI * i / steps;
            circlePoints.add(new Point(cx + r * Math.cos(a), cy + r * Math.sin(a)));
        }
        return new GestureTemplate("circle", circlePoints);
    }

    private GestureTemplate createTriangle() {
    ArrayList<Point> trianglePoints = new ArrayList<>();
    double xTop = 50, yTop = 10;
    double xLeft = 15, yLeft = 80;
    double xRight = 85, yRight = 80;
    addLinePoints(trianglePoints, xTop,  yTop,  xLeft,  yLeft, 24);
    addLinePoints(trianglePoints, xLeft, yLeft, xRight, yRight, 24);
    addLinePoints(trianglePoints, xRight, yRight, xTop,  yTop, 24);
    return new GestureTemplate("triangle", trianglePoints);
}

}
