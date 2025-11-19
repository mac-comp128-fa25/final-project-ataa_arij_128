package comp128.gestureRush;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.ui.Button;

import java.awt.Color;

public class endGame {

    private CanvasWindow canvas;
    private Button restartButton;

    public endGame() {
        canvas = new CanvasWindow("Restart", 600, 600);
        setupUI();
    }

    private void setupUI() {
        canvas.setBackground(Color.WHITE);

        GraphicsText title = new GraphicsText("Game Over !");
        title.setFillColor(Color.BLACK);
        title.setFontSize(32);
        title.setCenter(canvas.getWidth() / 2.0, 120);
        canvas.add(title);

        restartButton = new Button("Restart Game");
        startButton.setCenter(canvas.getWidth() / 2.0, 200);
        canvas.add(startButton);

        GraphicsText PreviousScore; = new GraphicsText(
            "Previous High score:"
        
        );

        // Score.scoreArray.remove();

        rules.setFillColor(Color.BLACK);
        rules.setFontSize(14);
        rules.setCenter(canvas.getWidth() / 2.0, 260);
        GraphicsText newScore = new GraphicsText(
            "New Score"
        );

        // Score.getCurrentScore

        //Score.consruct
        newScore.setFillColor(Color.WHITE);
        newScore.setFontSize(14);
        newScore.setCenter(canvas.getWidth() / 2.0, 290);
        canvas.add(rules);
        canvas.add(rulesTwo);

        startButton.onClick(() -> {
            canvas.closeWindow();
            new Menu();  
        });
    }
}
