package comp128.gestureRush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;

public class GestureRushGame {

    private final CanvasWindow canvas;
    private final List<GestureTemplate> templates;
    private Random rand = new Random();

    public GestureRushGame(){
        canvas = new CanvasWindow("GESTURE RUSH ", 600,600);
        templates = new ArrayList<>();
        templates.add(createArrow());
        templates.add(createCircle());
        templates.add(createTriangle());

        //spawnNext()
            //-falling gesture
        // update()

    }

    public void spawnNext(){
        GestureTemplate t = templates.get(rand.getnextInt(templates.size()));
        FallingGestures ft = new FallingGestures(t, canvas.getWidth(), canvas.getHeight());
        canvas.add(ft);
    }








}
