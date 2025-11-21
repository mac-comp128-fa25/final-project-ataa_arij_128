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
        this.eraserRing = new Ellipse(0,0, radius * 2, radius * 2);
        eraserRing.setStrokeColor(Color.RED);
        //eraserRing.setFillColor(Color.BLACK);
        canvas.add(eraserRing);
        eraserEvents();


    }

    private void eraserEvents(){
        canvas.onMouseDown(e -> {
            Point p = e.getPosition();
            eraserRing.setCenter(p.getX(), p.getY());
        });
        canvas.onMouseDown(e -> {
            isMouseDown = true;
            Point p = e.getPosition();
            eraserRing.setPosition(p.getX(), p.getY());
            int removed = erasePoints.onErase(p, radius);});

        canvas.onDrag(e ->{
            if (!isMouseDown){
                return;
            }
            Point p = e.getPosition();
            eraserRing.setCenter(p.getX(), p.getY());
            int removed = erasePoints.onErase(p,radius);

        });

        canvas.onMouseUp( e -> isMouseDown = false);


}
}