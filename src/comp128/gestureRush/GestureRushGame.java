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


   public GestureRushGame() {                 
       canvas = new CanvasWindow("Gesture Rush", 600, 600);
       canvas.setBackground(new Color(0xF0F0F0));
       templates = new ArrayList<>();
       templates.add(createArrowTemplate());
       templates.add(createCircleTemplate());
       templates.add(createTriangleTemplate());
       spawnNext();
       canvas.animate(this::update);
   }


   private void spawnNext() {
       GestureTemplate t = templates.get(rand.nextInt(templates.size()));
       currentGesture = new FallingGestures(t, canvas.getWidth());
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


   private GestureTemplate createArrowTemplate() {
       ArrayList<Point> pts = new ArrayList<>();
       pts.add(new Point(50, 10)); pts.add(new Point(50, 70));
       pts.add(new Point(50, 10)); pts.add(new Point(30, 30));
       pts.add(new Point(50, 10)); pts.add(new Point(70, 30)); 
       return new GestureTemplate("arrow", pts);
   }


   private GestureTemplate createCircleTemplate() {
       ArrayList<Point> pts = new ArrayList<>();
       double circleX = 50, cy = 50, r = 35;
       int lineSeg = 24;
       for (int i = 0; i <= seg; i++) {
           double a = 2 * Math.PI * i / seg;
           pts.add(new Point(cx + r * Math.cos(a), cy + r * Math.sin(a)));
       }
       return new GestureTemplate("circle", pts);
   }


   private GestureTemplate createTriangleTemplate() {
       ArrayList<Point> pts = new ArrayList<>();
       pts.add(new Point(50, 10));
       pts.add(new Point(15, 80));
       pts.add(new Point(85, 80));
       pts.add(new Point(50, 10));
       return new GestureTemplate("triangle", pts);
   }
}
