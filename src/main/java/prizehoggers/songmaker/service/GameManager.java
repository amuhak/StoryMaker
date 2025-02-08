package prizehoggers.songmaker.service;

public class GameManager {
    StringBuilder words = new StringBuilder();
    long lastUpdate = 0;

    public String getWords() {
        return words.toString();
    }

    public void addWord(String word) {
        words.append(word).append(" ");
        timeReset();
    }

    public boolean overdue() {
        return (System.currentTimeMillis() / 1000) - lastUpdate > 10;
    }

    public void timeReset() {
        lastUpdate = System.currentTimeMillis() / 1000;
    }
}
