package comp128.gestureRush;

public class GameResult {

    private final int score;
    private final Medal medal;

    public GameResult(int score, Medal medal) {
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
