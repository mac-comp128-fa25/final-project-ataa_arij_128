package comp128.gestureRush;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GestureRushGame {

    private final CanvasWindow canvas;
    private FallingGestures currentGesture;
    private final List<GestureTemplate> templates;
    private final Random rand = new Random();

    private double shapeSize = 90;

    public GestureRushGame() {
        canvas = new CanvasWindow("Gesture Rush", 600, 600);
        canvas.setBackground(Color.WHITE);
        templates = new ArrayList<>();
        templates.add(createArrow());
        templates.add(createCircle());
        templates.add(createTriangle());

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
        // if we cant to decrease shape size as it falls shapeSize *= 0.95 * dt;

        if (currentGesture != null) {
            boolean stillOn = currentGesture.update(dt, canvas.getHeight());
            if (!stillOn) {
                canvas.remove(currentGesture);
                currentGesture = null;
                spawnNext();
            }
        }
    }

    private GestureTemplate createArrow() {
        ArrayList<Point> arrowPoint = new ArrayList<>();
        arrowPoint.add(new Point(50, 10)); arrowPoint.add(new Point(50, 70));
        arrowPoint.add(new Point(50, 10)); arrowPoint.add(new Point(30, 30));
        arrowPoint.add(new Point(50, 10)); arrowPoint.add(new Point(70, 30)); 
        return new GestureTemplate("arrow", arrowPoint);
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
        trianglePoints.add(new Point(50, 10));
        trianglePoints.add(new Point(15, 80));
        trianglePoints.add(new Point(85, 80));
        trianglePoints.add(new Point(50, 10));
        return new GestureTemplate("triangle", trianglePoints);
    }
}
