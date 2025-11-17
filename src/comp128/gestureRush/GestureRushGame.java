package comp128.gestureRush;

public class GestureRushGame {

    private final CanvasWindow canvas;
    private final List<GestureTemplate> templates;
    private Random rand = new Random(3);

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








}
