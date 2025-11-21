package comp128.gestureRush;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Point;

import java.awt.Color;

public class PlayerEraser {

    public interface EraseCallback {
        int onEraseAt(Point absolutePoint, double radius);
    }

    private final CanvasWindow canvas;
    private final EraseCallback callback;
    private final double radius;

    private final Ellipse ring;
    private boolean mouseDown = false;

    public PlayerEraser(CanvasWindow canvas, EraseCallback callback, double radius) {
        this.canvas = canvas;
        this.callback = callback;
        this.radius = radius;

        ring = new Ellipse(0, 0, radius * 2, radius * 2);
        ring.setStrokeColor(new Color(0, 100, 255, 160));
        ring.setFillColor(new Color(0, 0, 0, 0));
        canvas.add(ring);

        hookEvents();
    }

    private void hookEvents() {
        // Always move the ring with the mouse
        canvas.onMouseMove(e -> {
            Point p = e.getPosition();
            ring.setCenter(p.getX(), p.getY());
        });

        // Start erasing on mouse down (still supports single click)
        canvas.onMouseDown(e -> {
            mouseDown = true;
            Point p = e.getPosition();
            ring.setCenter(p.getX(), p.getY());
            int removed = callback.onEraseAt(p, radius);
            if (removed > 0) {
                System.out.println("onMouseDown erased " + removed + " segments at " + p);
            }
        });

        // While mouse is held and dragged, erase continuously
        canvas.onDrag(e -> {
            if (!mouseDown) return;   // safety, probably always true during drag
            Point p = e.getPosition();
            ring.setCenter(p.getX(), p.getY());
            int removed = callback.onEraseAt(p, radius);
            if (removed > 0) {
                System.out.println("onDrag erased " + removed + " segments at " + p);
            }
        });

        // Stop erasing when mouse button is released
        canvas.onMouseUp(e -> mouseDown = false);
    }
}
