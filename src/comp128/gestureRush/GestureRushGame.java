package comp128.gestureRush;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Point;

public class GestureRushGame {

    private final CanvasWindow CANVAS;
    private FallingGestures currentGesture;
    private final List<GestureTemplate> TEMPLATES; // Affects of Deque v List (can talk about in report)
    private final Random rand = new Random();

    private double shapeSize = 90;
    private PlayerEraser eraser;
    private double eraserRadius = 5;
    private final ArrayList<Point> removedLog = new ArrayList<>(); // won't this list be changing (should it be final)
    private GraphicsText scoreText;
    private GraphicsText prevScoreText;
    private GraphicsText highScoreText;
    private GraphicsText missedPointsText;
    private Timer timer;
    private TimerTask task; // 1 timer for number of gestures falling, another for which gestures falling
    //private Deque<FallingGestures> currentGestures; // Deque that stores the current gestures
    //private int numGestures;
    private int[] scores; // Data Structure that stores the scores. Must choose most efficient structue
    private int missedPoints;
    private int score; 
    private Score scoreManager;
    private endGame endGame;

    public GestureRushGame() {
       // currentGestures = new ArrayDeque<>();
       // numGestures = 1;
        scoreManager = new Score();
        timer = new Timer();
        task = new TimerTask() { // Currently has no effect, after certain amount of time more gestures start falling
            @Override
            public void run(){
             //   numGestures++;
            }
        };
        timer.schedule(task, 2000); // Delay will be 15000 (15 seconds) 
        CANVAS = new CanvasWindow("Gesture Rush", 600, 600);
        CANVAS.setBackground(Color.WHITE);
        TEMPLATES = new ArrayList<>();
        TEMPLATES.add(createArrow());
        TEMPLATES.add(createCircle());
        TEMPLATES.add(createTriangle());

        eraser = new PlayerEraser(CANVAS, (p, r) -> {
            if (currentGesture == null) return 0;
            int removed = currentGesture.eraseAt(p, r, removedLog); // Every point (where mouse clicks) being added to list regardless whether it's a gesture point or not
            if (removed > 0 && currentGesture.isFullyErased()) {
                CANVAS.remove(currentGesture);
                currentGesture = null;
                spawnNext();
            }
            // if (removed > 0 && !currentGesture.isFullyErased()){
            //     currentGesture.getTemplate().getPoints().size();
            // }
            // System.out.println(removedLog);
            return removed;
        }, eraserRadius);

        score = 0;
        missedPoints = 0;
        
        scoreText = new GraphicsText("Score: ", 10, 30);
        scoreText.setFontSize(32);
        scoreText.setFillColor(Color.BLUE);

        prevScoreText = new GraphicsText("Previous: ", CANVAS.getWidth()/3, 30);
        prevScoreText.setFontSize(32);
        prevScoreText.setFillColor(Color.BLUE);

        highScoreText = new GraphicsText("High: ", CANVAS.getWidth() - 200, 30);
        highScoreText.setFontSize(32);
        highScoreText.setFillColor(Color.BLUE);

        missedPointsText = new GraphicsText("Missed: ", 10, 70);
        missedPointsText.setFontSize(16);
        missedPointsText.setFillColor(Color.BLUE);

        CANVAS.add(scoreText);
        CANVAS.add(prevScoreText);
        CANVAS.add(highScoreText);
        CANVAS.add(missedPointsText);

        spawnNext();
        CANVAS.animate(this::update);
    }

    /**
     * Method for getting new gestures to start falling down the screen
     */
    private void spawnNext() {
        GestureTemplate t = TEMPLATES.get(rand.nextInt(TEMPLATES.size()));
        currentGesture = new FallingGestures(t, CANVAS.getWidth(), shapeSize);
        CANVAS.add(currentGesture);
    }

    /**
     * updates the position of the falling gesture and spawns the next gesture once the current gesture no longer exists
     */
    private void update() {
        double dt = 0.025;
        if (currentGesture != null) {
            boolean stillOn = currentGesture.update(dt, CANVAS.getHeight());

            if (!stillOn) {
                CANVAS.remove(currentGesture);
                missedPoints += currentGesture.getTemplate().getPoints().size() - eraser.getTempRemovedPoints(); 
                missedPointsText.setText("Missed: " + missedPoints + " Points!!!");
                currentGesture = null;
                spawnNext();
            }
        }
        score = eraser.getRemovedPoints();
        scoreText.setText("Score: " + score);
        if (missedPoints >= 200){ // Once player misses 200 points, game ends
            scores[0] = score; // Put score into Score Manager, (Choose data structure for this/delete if not necessary)
            CANVAS.closeWindow();
            endGame = new endGame(scoreManager);
        }
    }
    
    /**
     * helper method that creates line segments between points (is this accurate)
     * @param lineseg
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param steps
     */
    private void addLinePoints(List<Point> lineseg, double x1, double y1, double x2, double y2, int steps){
        for (int i = 0; i <= steps; i++) {
        double t = i / (double) steps;
        double x = x1 + t * (x2 - x1);
        double y = y1 + t * (y2 - y1);
        lineseg.add(new Point(x, y));
        }
    }
                            

    /**
     * creates arrow gesture
     * @return arrow template
     */
    private GestureTemplate createArrow() {
        ArrayList<Point> arrowPoints = new ArrayList<>(); // Data structure question to consider, will the program run whether these are stored in lists or deques, will time complexity change if stored in diff. structure
        addLinePoints(arrowPoints, 50, 10, 50, 70, 24); 
        addLinePoints(arrowPoints, 50, 10, 30, 30, 16);
        addLinePoints(arrowPoints, 50, 10, 70, 30, 16); 
        return new GestureTemplate("arrow", arrowPoints);
    }


    /**
     * creates circle gesture
     * @return circle template
     */
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

    /**
     * creates triangle gesture
     * @return triangle template
     */
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

    // method for end game 
    // add score to scores

    // Arrow has 59 points, Circle has 33, Triangle has 75

}
