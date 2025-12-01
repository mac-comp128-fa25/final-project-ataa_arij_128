package comp128.gestureRush;

public class Medal {

    private final String name;
    private final int minScore;

    private Medal(String name, int minScore) {
        this.name = name;
        this.minScore = minScore;
    }

    private static final Medal[] MEDAL_LEVELS = {
        new Medal("Bronze", 0),   
        new Medal("Silver", 20),  
        new Medal("Gold",  40)   
    };

    public String getName() {
        return this.name;
    }

    public int getMinScore() {
        return this.minScore;
    }

    public static Medal currScore(int score) {
        Medal chosenMedal = MEDAL_LEVELS[0];
        for (int i = 0; i < MEDAL_LEVELS.length; i++) {
            if (score >= MEDAL_LEVELS[i].minScore) {
                chosenMedal = MEDAL_LEVELS[i];
            }
        }
        return chosenMedal;
    }







}
