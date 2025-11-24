package comp128.gestureRush;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Point;
import java.awt.Color;

public class PlayerEraser {

    public interface EraseCallback {
        int onErase(Point p, double r);
    }

    private final CanvasWindow canvas;
    private final EraseCallback erasePoints;
    public final double radius;
    private final Ellipse eraserRing;
    private boolean isMouseDown = false;

    public PlayerEraser(CanvasWindow canvas, EraseCallback c, double radius){
        this.canvas = canvas;
        this.erasePoints = c;
        this.radius = radius;

        this.eraserRing = new Ellipse(0, 0, radius * 2, radius * 2);
        eraserRing.setStrokeColor(Color.RED);
        eraserRing.setFillColor(new Color(0, 0, 0, 0));
        canvas.add(eraserRing);

        hookEvents();
    }

    private void hookEvents(){
        canvas.onMouseDown(e -> {
            isMouseDown = true;
            Point p = e.getPosition();
            eraserRing.setCenter(p.getX(), p.getY());
            int removed = erasePoints.onErase(p, radius);
            if (removed > 0) {
                System.out.println("removed " + removed);
            }
        });
        canvas.onDrag(e -> {
            if (!isMouseDown) return;
            Point p = e.getPosition();
            eraserRing.setCenter(p.getX(), p.getY());
            int removed = erasePoints.onErase(p, radius);
            if (removed > 0) {
                System.out.println("removed " + removed);
            }
        });

        canvas.onMouseUp(e -> isMouseDown = false);
    }
}
