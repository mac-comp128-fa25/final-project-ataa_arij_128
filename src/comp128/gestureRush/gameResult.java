package comp128.gestureRush;

public class gameResult {

    private final int score;
    private final Medal medal;

    public gameResult(int score, Medal medal) {
        this.score = score;
        this.medal = medal;
    }

    public int getScore() {
        return score;
    }

    public Medal getMedal() {
        return medal;
    }
}
