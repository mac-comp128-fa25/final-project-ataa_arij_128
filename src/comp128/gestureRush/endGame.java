package comp128.gestureRush;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.ui.Button;

/**
 * Class for end game menu
 */
public class endGame {

    private final CanvasWindow canvas;
    private final Score scoreManager;

    public endGame(Score scoreManager) {
        this.scoreManager = scoreManager;
        this.canvas = new CanvasWindow("Gesture Rush Results", 600, 600);
        setupUI();
    }

    private void setupUI() {
        canvas.setBackground(Color.WHITE);
        GraphicsText title = new GraphicsText("Game Over!");
        title.setFontSize(32);
        title.setFillColor(Color.BLACK);
        title.setCenter(canvas.getWidth() / 2.0, 80);
        canvas.add(title);

        int current = scoreManager.getCurrentScore();
        int best = scoreManager.getTopScore();
        GraphicsText currentText = new GraphicsText("Current Score: " + current);
        currentText.setFontSize(18);
        currentText.setFillColor(Color.BLACK);
        currentText.setPosition(80, 140);
        canvas.add(currentText);

        GraphicsText bestText = new GraphicsText("Top Score: " + best);
        bestText.setFontSize(18);
        bestText.setFillColor(new Color(40, 40, 160));
        bestText.setPosition(330, 140);
        canvas.add(bestText);
        double Y = 210;

        GraphicsText header = new GraphicsText("Previous Games: ");
        header.setFontSize(16);
        header.setFillColor(Color.DARK_GRAY);
        header.setPosition(80, Y - 25);
        canvas.add(header);

        Line headerLine = new Line(70, Y - 5, canvas.getWidth() - 70, Y - 5);
        headerLine.setStrokeColor(Color.LIGHT_GRAY);
        canvas.add(headerLine);

        GraphicsText colGame = new GraphicsText("Game #");
        colGame.setFontSize(14);
        colGame.setFillColor(Color.BLACK);
        colGame.setPosition(90, Y + 10);
        canvas.add(colGame);

        GraphicsText colScore = new GraphicsText("Score");
        colScore.setFontSize(14);
        colScore.setFillColor(Color.BLACK);
        colScore.setPosition(220, Y + 10 );
        canvas.add(colScore);
        
        GraphicsText columnMedal = new GraphicsText("Medal");
        columnMedal.setFontSize(14);
        columnMedal.setFillColor(Color.BLACK);
        columnMedal.setPosition(360, Y + 10);
        canvas.add(columnMedal);
        Line columnLine = new Line(70, Y + 18, canvas.getWidth() - 70, Y + 18);
        columnLine.setStrokeColor(Color.LIGHT_GRAY);
        canvas.add(columnLine);
        GameResult[] history = scoreManager.getHistory();
        double rowY = Y + 40;
        int gameIndex = 1;

        for (GameResult result : history) {
            GraphicsText gameIndexText = new GraphicsText("#" + gameIndex);
            gameIndexText.setFontSize(14);
            gameIndexText.setFillColor(Color.BLACK);
            gameIndexText.setPosition(100, rowY);
            canvas.add(gameIndexText);
            GraphicsText scoreText = new GraphicsText(String.valueOf(result.getScore()));
            scoreText.setFontSize(14);
            scoreText.setFillColor(Color.BLACK);
            scoreText.setPosition(230, rowY);
            canvas.add(scoreText);
            String medalName = result.getMedal().getName();
            GraphicsText medalText = new GraphicsText(medalName);
            medalText.setFontSize(14);
            medalText.setFillColor(Color.BLACK);
            medalText.setPosition(360, rowY);
            canvas.add(medalText);
            rowY += 28;
            gameIndex++;
        }

        Button restartButton = new Button("Restart");
        restartButton.setCenter(canvas.getWidth() / 2.0, 520);
        canvas.add(restartButton);

        restartButton.onClick(() -> {
            canvas.closeWindow();
            scoreManager.resetNewGame();
            new GestureRushGame(scoreManager);
        });
    }
}
