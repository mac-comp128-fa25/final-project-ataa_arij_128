package comp128.gestureRush; 

import java.awt.Color;
import java.util.List;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;

public class FallingGestures extends GraphicsGroup {
    private final double speed;
    private double size;
    // private GestureTemplate template;

    public FallingGestures(GestureTemplate template, double canvasWidth, double shapeSize) {
        this.size = shapeSize;
        // this.template = template;
        double x = 50 + Math.random() * (canvasWidth - 100);
        double y = -size - 10;

        List<Point> templatePoints = template.getPoints();
        if (templatePoints != null && templatePoints.size() > 1) {
            for (int i = 0; i < templatePoints.size() - 1; i++) {
                Point point1 = templatePoints.get(i);
                Point point2 = templatePoints.get(i + 1);
                Line gestureLine = new Line(
                    point1.getX() / 100.0 * size, point1.getY() / 100.0 * size,
                    point2.getX() / 100.0 * size, point2.getY() / 100.0 * size
                );
                gestureLine.setStrokeColor(Color.BLACK);
                gestureLine.setStrokeWidth(4); 
                add(gestureLine);
            }
        } 
        setPosition(x, y);
        speed = 120 + Math.random() * 78;
    }

    public boolean update(double x, double canvasHeight) {
        moveBy(0, speed * x);
        return getY() < canvasHeight;
    }

    //public GestureTemplate getTemplate() or public List<Point> getCurrPoints

    
}
