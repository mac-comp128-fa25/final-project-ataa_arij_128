package comp128.gestureRush;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.ui.Button;

public class Menu {

    private CanvasWindow canvas;
    private Button startButton;

    public Menu() {
        canvas = new CanvasWindow("Gesture Rush Menu", 600, 600);
        setupUI();
    }

    /**
     * sets up menu screen
     */
    private void setupUI() {
        canvas.setBackground(Color.BLACK);

        GraphicsText title = new GraphicsText("Gesture Rush");
        title.setFillColor(Color.BLUE);
        title.setFontSize(32);
        title.setCenter(canvas.getWidth() / 2.0, 120);
        canvas.add(title);

        startButton = new Button("Start Game");
        startButton.setCenter(canvas.getWidth() / 2.0, 200);
        canvas.add(startButton);

        GraphicsText rules = new GraphicsText(
            "Rules: One gesture falls from the top with increasing speed,"
        
        );
        rules.setFillColor(Color.WHITE);
        rules.setFontSize(14);
        rules.setCenter(canvas.getWidth() / 2.0, 260);
         GraphicsText rulesTwo = new GraphicsText(
            "draw to match before it hits the bottom!"
        );
        rulesTwo.setFillColor(Color.WHITE);
        rulesTwo.setFontSize(14);
        rulesTwo.setCenter(canvas.getWidth() / 2.0, 290);
        canvas.add(rules);
        canvas.add(rulesTwo);

        startButton.onClick(() -> {
            canvas.closeWindow();
            new GestureRushGame();  
        });
    }
}
