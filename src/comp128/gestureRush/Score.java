package comp128.gestureRush;

import java.util.ArrayDeque;
import java.util.Queue;

public class Score {

   
    private static final int HISTORY_SIZE = 5;
    private int currentScore;
    private int topScore;
    private final Queue<GameResult> history = new ArrayDeque<>(HISTORY_SIZE);

    public Score(){}

    public void resetNewGame() {
        currentScore = 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getTopScore() {
        return topScore;
    }

    public void calculatePoints(int erasedPoints, int totalPoints) {
        if (totalPoints <= 0) {
            return;
        }
        double fraction = (double) erasedPoints / totalPoints;
        if (fraction < 0) fraction = 0;
        if (fraction > 1) fraction = 1;
        int gesturePoints = (int) Math.round(5.0 * fraction);

        currentScore += gesturePoints;
    }

    public GameResult finishedGame() {
        Medal medal = Medal.currScore(currentScore);
        GameResult result = new GameResult(currentScore, medal);

        if (currentScore > topScore) {
            topScore = currentScore;
        }

        addToHistory(result);
        return result;
    }

    public int getHistoryCount() {
        return history.size();
    }

    public GameResult[] getHistory() {
        int size = history.size();
        GameResult[] result = new GameResult[size];
        int i = size - 1;
        for (GameResult gr : history) {
            result[i] = gr;
            i--;
        }
        return result;
    }

   
    private void addToHistory(GameResult result) {
        if (history.size() == HISTORY_SIZE) {
            history.poll();   // O(1)
        }
        history.offer(result); // O(1), add at tail
    }
}
