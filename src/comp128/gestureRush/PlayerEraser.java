package comp128.gestureRush;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Point;

public class PlayerEraser {

    public interface EraseCallback {
        int onErase(Point p, double r);
    }

    private final CanvasWindow CANVAS;
    private final EraseCallback POINTS; //erase points
    public final double RADIUS;
    private final Ellipse RING;
    private boolean isMouseDown = false;
    private int numRemovedPoints;

    public PlayerEraser(CanvasWindow canvas, EraseCallback c, double radius){
        this.CANVAS = canvas;
        this.POINTS = c;
        this.RADIUS = radius;

        this.RING = new Ellipse(0, 0, radius * 2, radius * 2);
        RING.setStrokeColor(Color.RED);
        RING.setFillColor(new Color(0, 0, 0, 0));
        canvas.add(RING);
        numRemovedPoints = 0;

        hookEvents();
    }

    /**
     * Erases points from gesture
     */
    private void hookEvents(){
        CANVAS.onMouseDown(e -> {
            isMouseDown = true;
            Point p = e.getPosition();
            RING.setCenter(p.getX(), p.getY());
            int removed = POINTS.onErase(p, RADIUS); // shows amount of points removed on single erase
            if (removed > 0) {
                System.out.println("removed " + removed); 
            }
        });
        CANVAS.onDrag(e -> {
            if (!isMouseDown) return;
            Point p = e.getPosition();
            RING.setCenter(p.getX(), p.getY());
            int removed = POINTS.onErase(p, RADIUS);
            if (removed > 0) {
                //System.out.println("removed " + removed);
                numRemovedPoints += removed; 
            }
        });

        CANVAS.onMouseUp(e -> {
            isMouseDown = false;
            //System.out.println("Total Removed Points: " + numRemovedPoints);
        });
    }

    public int getRemovedPoints(){ // Using this for the score
        return numRemovedPoints;
    }
}
