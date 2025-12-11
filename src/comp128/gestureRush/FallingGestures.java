package comp128.gestureRush; 

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;

public class FallingGestures extends GraphicsGroup {
    private final double SPEED;
    private double size;
    private final ArrayList<Line> SEGMENTS = new ArrayList<>();
    private int aliveSegments;
    private final GestureTemplate template;

    public FallingGestures(GestureTemplate template, double canvasWidth, double shapeSize, double speedMultiplier) {
        this.size = shapeSize;
        this.template = template;

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
                gestureLine.setStrokeWidth(6); 
                SEGMENTS.add(gestureLine);
                add(gestureLine);
            }
        } 
        aliveSegments = SEGMENTS.size();

        setPosition(x, y);
        SPEED = (120 + Math.random() * 78) * speedMultiplier;
    }

    /**
     * Method to update the position of the falling gesture
     * @param dt 
     * @param canvasHeight
     * @return Y less then canvas height
     */
    public boolean update(double dt, double canvasHeight) {
        moveBy(0, SPEED * dt);
        return getY() < canvasHeight;
    }
    
    /**
     * Method for erased points of fallen gesture
     * @param pAbs absolute point in canvas coordinates
     * @param radius eraser radius
     * @param removedSink where we log removed midpoints (for potential scoring/analysis)
     * @return number of segments removed
     */
    public int eraseAt(Point pAbs, double radius, List<Point> removedSink) {
        if (aliveSegments <= 0) return 0;

        // Convert to local coordinates of this group
        double px = pAbs.getX() - getX();
        double py = pAbs.getY() - getY();
        double r2 = radius * radius;

        int removed = 0;

        for (int i = 0; i < SEGMENTS.size(); i++) {
            Line seg = SEGMENTS.get(i);
            if (seg == null) continue;

            double ax = seg.getX1(), ay = seg.getY1();
            double bx = seg.getX2(), by = seg.getY2();

            double distSq = distanceToSegmentSquared(px, py, ax, ay, bx, by);
            if (distSq <= r2) {
                remove(seg);
                SEGMENTS.set(i, null);
                aliveSegments--;
                removed++;

                double mx = (ax + bx) * 0.5 + getX();
                double my = (ay + by) * 0.5 + getY();
                removedSink.add(new Point(mx, my));
            }
        }
        return removed;
    }  

    /**
     * This acts as a  helper algorithm that computes the squared distance between a point and a line segment within
     * a gesture.
     */
    private static double distanceToSegmentSquared(double px, double py,
                                                   double ax, double ay,
                                                   double bx, double by) {
        double abx = bx - ax;
        double aby = by - ay;
        double apx = px - ax;
        double apy = py - ay;

        double abLengthSq = abx * abx + aby * aby;
        if (abLengthSq == 0) {
            double dx = px - ax;
            double dy = py - ay;
            return dx * dx + dy * dy;
        }
        double t = (apx * abx + apy * aby) / abLengthSq;
        if (t < 0) {
            t = 0;
        } else if (t > 1) {
            t = 1;
        }

        double closestX = ax + t * abx;
        double closestY = ay + t * aby;
        double dx = px - closestX;
        double dy = py - closestY;
        return dx * dx + dy * dy;
    }

    /**
     * this method tells us if the Falling gesture has been fully erased
     * @return alive segments
     */
    public boolean isFullyErased() {
        return aliveSegments <= 0;
    }

    public GestureTemplate getTemplate(){
        return template;
    }

    /*Shows how many total number of line segments for scoring. */
    public int getTotalSegments() {
        return SEGMENTS.size();
    }

    /*This method aims to show many segments are still visible. */
    public int getAliveSegments() {
        return aliveSegments;
    }

    /* This shows many gesture segments have been erased thus far. */
    public int getErasedSegments() {
        return SEGMENTS.size() - aliveSegments;
    }
}
