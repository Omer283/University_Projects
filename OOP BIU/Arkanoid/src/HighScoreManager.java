import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The type High score manager.
 */
public class HighScoreManager {
    private final File hs = new File("resources\\highscores.txt");

    /**
     * Gets high score.
     *
     * @return the high score
     */
    public int getHighScore() {
        try {
            if (hs.createNewFile()) {
                return 0;
            } else {
                BufferedReader br = new BufferedReader(new FileReader(hs));
                String score = br.readLine();
                if (score == null) {
                    return 0;
                }
                String[] words = score.split("\\s+");
                return Integer.parseInt(words[words.length - 1]);
            }
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * Updates high score.
     *
     * @param score the new score
     * @return the true iff changed
     */
    public boolean update(int score) {
        try {
            if (hs.createNewFile() || score > getHighScore()) {
                BufferedWriter bf = new BufferedWriter(new FileWriter(hs));
                bf.write("The highest score so far is: " + score);
                bf.close();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

}
