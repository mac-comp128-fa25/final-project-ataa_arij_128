package comp128.gestureRush;

public class Score {
    
    private static final int SCORE_HISTORY = 5;
    private static final int HISTORY_SIZE = 10; // Subject to change
    
    private int currentScore;
    private int topScore;
    private gameResult[] history = new gameResult[SCORE_HISTORY];
    private int numOfGames;
    private int historyCount;
    private int historyStart;

    public Score(){
        this.historyCount = 0;
        this.historyStart = 0;
    }
    
    private void resetNewGame(){
        currentScore = 0;
    }

    public int getHistoryNum(){
        return this.numOfGames;
    }

    public int getCurrentScore(){
        return this.currentScore;
    }

    public int getTopScore(){
        return this.topScore;
    }

    public void calculatePoints(int erasedPoints, int totalPoints){
        double gesturePoints = 0;
        if (erasedPoints <= 0){
            return; // Might not need this statement
        }
        else{
            gesturePoints = erasedPoints/totalPoints;
            gesturePoints = Math.round(gesturePoints) * 5;
            currentScore += gesturePoints;
        }
    }

    public gameResult[] getHistory() {
        gameResult[] result = new gameResult[historyCount];
        for (int i = 0; i < historyCount; i++) {
            // newest is at index: (historyStart + historyCount - 1 - i) mod HISTORY_SIZE
            int index = (historyStart + historyCount - 1 - i + HISTORY_SIZE) % HISTORY_SIZE;
            result[i] = history[index];
        }
        return result;
    }

    private void addToHistory(gameResult result) {
        if (historyCount < HISTORY_SIZE) {
            int index = (historyStart + historyCount) % HISTORY_SIZE;
            history[index] = result;
            historyCount++;
        } 
        else {
            history[historyStart] = result;
            historyStart = (historyStart + 1) % HISTORY_SIZE;
        }
    }

    public gameResult finishedGame() {
        Medal medal = Medal.currScore(currentScore);
        gameResult result = new gameResult(currentScore, medal);
        if (currentScore > topScore) {
            topScore = currentScore;
        }
        addToHistory(result);
        return result;
    }
    
}
