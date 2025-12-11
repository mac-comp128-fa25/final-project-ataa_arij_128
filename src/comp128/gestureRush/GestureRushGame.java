package comp128.gestureRush;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Point;

public class GestureRushGame {

    private final CanvasWindow CANVAS;
    private FallingGestures currentGesture;
    private final List<GestureTemplate> TEMPLATES; 
    private final Random rand = new Random();

    private double shapeSize = 90;
    private double speedMultiplier = 1.0;
    private final PlayerEraser eraser;
    private final double eraserRadius = 5;
    private final ArrayList<Point> removedLog = new ArrayList<>(); // Stores removed coords for scoring logic

    private final Score scoreManager;

    // UI text for live display
    private final GraphicsText scoreText;
    private final GraphicsText timerText;
    private final GraphicsText highScoreText;   //top score display
    private final GraphicsText missedText;      //missed points display

    // Game runs for 1 minute 30 seconds as of now
    private double timeRemaining = 90.0; 
    private boolean gameOver = false;

    // Once player misses enough points (segments), game ends
    private int missedPoints = 0;
    private static final int MISSED_LIMIT = 200; 

    public GestureRushGame(Score scoreManager) {
        // Shared score
        this.scoreManager = scoreManager;
        this.scoreManager.resetNewGame();
        missedPoints = 0;
        speedMultiplier = 1.0;

        CANVAS = new CanvasWindow("Gesture Rush", 600, 600);
        CANVAS.setBackground(Color.WHITE);

        TEMPLATES = new ArrayList<>();
        TEMPLATES.add(createArrow());
        TEMPLATES.add(createCircle());
        TEMPLATES.add(createLightning());
        TEMPLATES.add(createZShape());
        TEMPLATES.add(createTriangle());
        TEMPLATES.add(createCheckmark());
        

        eraser = new PlayerEraser(CANVAS, (p, r) -> {
            if (currentGesture == null || gameOver) return 0;
            // Every erase request tries to remove segments from the current gesture
            int removed = currentGesture.eraseAt(p, r, removedLog);
            if (removed > 0 && currentGesture.isFullyErased()) {
                // Gesture fully erased and hence we award points based on the fraction erased
                int total = currentGesture.getTotalSegments();
                int erasedSegs = currentGesture.getErasedSegments();
                scoreManager.calculatePoints(erasedSegs, total);
                updateScoreLabel();

                CANVAS.remove(currentGesture);
                currentGesture = null;
                spawnNext();
            }
            return removed;
        }, eraserRadius);

        // Score display
        scoreText = new GraphicsText("Score: 0", 10, 30);
        scoreText.setFontSize(24);
        scoreText.setFillColor(Color.BLACK);
        CANVAS.add(scoreText);

        // Timer display (1:30 initially)
        timerText = new GraphicsText("Time: 01:30", CANVAS.getWidth() - 160, 30);
        timerText.setFontSize(24);
        timerText.setFillColor(Color.RED);
        CANVAS.add(timerText);

        highScoreText = new GraphicsText("Top: " + scoreManager.getTopScore(), 10, 60);
        highScoreText.setFontSize(18);
        highScoreText.setFillColor(new Color(40, 40, 160));
        CANVAS.add(highScoreText);

        missedText = new GraphicsText("Missed: 0", 10, 90);
        missedText.setFontSize(18);
        missedText.setFillColor(Color.DARK_GRAY);
        CANVAS.add(missedText);

        spawnNext();
        CANVAS.animate(this::update);
    }

    /**
     * Method for getting new gestures to start falling down the screen
     */
    private void spawnNext() {
        GestureTemplate t = TEMPLATES.get(rand.nextInt(TEMPLATES.size()));
        currentGesture = new FallingGestures(t, CANVAS.getWidth(), shapeSize, speedMultiplier);
        CANVAS.add(currentGesture);
    }

    /**
     * Updates the position of the falling gesture and spawns the next gesture once the current gesture no longer exists
     * Also decrements the game timer and ends the game when time is up.
     */
    private void update() {
        if (gameOver) {
            return;
        }

        double dt = 0.025;

        timeRemaining -= dt;
        if (timeRemaining < 0) {
            timeRemaining = 0;
        }
        updateTimerLabel();

        if (timeRemaining <= 0) {
            endGameNow();
            return;
        }

        if (currentGesture != null) {
            boolean stillOn = currentGesture.update(dt, CANVAS.getHeight());

            if (!stillOn) {
                // as the respective Gesture falls off screen, we award points based on how much was erased
                int total = currentGesture.getTotalSegments();
                int erasedSegs = currentGesture.getErasedSegments();
                int missed = total - erasedSegs;
                if (missed < 0) missed = 0; // safety

                scoreManager.calculatePoints(erasedSegs, total);
                updateScoreLabel();
                updateSpeedMultiplier();

                // we accumulate the missed segments; if too many, game over
                missedPoints += missed;
                missedText.setText("Missed: " + missedPoints);

                CANVAS.remove(currentGesture);
                currentGesture = null;

                if (missedPoints >= MISSED_LIMIT) {
                    endGameNow();
                    return;
                }

                spawnNext();
            }
        }
    }

    /**
     * Helper updates score label
     */
    private void updateScoreLabel() {
        int current = scoreManager.getCurrentScore();
        int top = scoreManager.getTopScore();
        // Show live "top" as the max of historical top and current run,
        // so the player sees when they've beaten the old top score.
        int displayedTop = Math.max(top, current);

        scoreText.setText("Score: " + current);
        highScoreText.setText("Top: " + displayedTop);
    }

    /**
     * Helper updates timer label
     */
    private void updateTimerLabel() {
        int totalSeconds = (int) Math.ceil(timeRemaining);
        if (totalSeconds < 0) totalSeconds = 0;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String text = String.format("%02d:%02d", minutes, seconds);
        timerText.setText("Time: " + text);
    }
    
    /**
     * helper increases speed based on score
     */
    private void updateSpeedMultiplier() {
        int currentScore = scoreManager.getCurrentScore();
        if (currentScore >= 60) {
            speedMultiplier = 1.4;
        }
        else if (currentScore >= 40) {
            speedMultiplier = 1.3;  
        } else if (currentScore >= 20) {
            speedMultiplier = 1.15; 
        } else {
            speedMultiplier = 1.0;}
    }

    /**
     * Cleanly end the game: score current gesture, finalize score, show end screen.
     */
    private void endGameNow() {
        gameOver = true;

        if (currentGesture != null) {
            int total = currentGesture.getTotalSegments();
            int erasedSegs = currentGesture.getErasedSegments();
            scoreManager.calculatePoints(erasedSegs, total);
            updateScoreLabel();
        }

        // finalize the result → compute medal, update history + top score
        scoreManager.finishedGame();

        CANVAS.closeWindow();
        new endGame(scoreManager);
    }
    
    /**
     * this is a helper method that creates line segments between points (is this accurate)
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
        ArrayList<Point> arrowPoints = new ArrayList<>(); 
        addLinePoints(arrowPoints, 50, 10, 50, 70, 24); 
        addLinePoints(arrowPoints, 50, 10, 30, 30, 16);
        addLinePoints(arrowPoints, 50, 10, 70, 30, 16); 
        return new GestureTemplate("arrow", arrowPoints);
    }

    /**
     * creates a checkmark shape
     * @return checkmark template
     */
    private GestureTemplate createCheckmark() {
        ArrayList<Point> checkPoints = new ArrayList<>();
        addLinePoints(checkPoints, 25, 30, 20, 70, 20);
        addLinePoints(checkPoints, 45, 70, 80, 20, 25);
        return new GestureTemplate("check", checkPoints);
    }

    /**
     * a zig zag-esque shape
     * @return z shape
     */
    private GestureTemplate createZShape() {
        ArrayList<Point> zPoints = new ArrayList<>();
        addLinePoints(zPoints, 20, 30, 40, 70, 15);
        addLinePoints(zPoints, 40, 70, 60, 30, 15);
        addLinePoints(zPoints, 60, 30, 80, 70, 15);
        return new GestureTemplate("zigzag", zPoints);
    }

    /**
     * creates a shape resembling a lightning bolt/flash
     * @return lightning
     */
    private GestureTemplate createLightning() {
        ArrayList<Point> boltPoints = new ArrayList<>();
        addLinePoints(boltPoints, 50, 10, 35, 40, 15);
        addLinePoints(boltPoints, 35, 40, 55, 50, 15);
        addLinePoints(boltPoints, 55, 50, 40, 80, 15);
        return new GestureTemplate("lightning", boltPoints);
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
}
