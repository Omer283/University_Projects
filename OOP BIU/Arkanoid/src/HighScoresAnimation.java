import biuoop.DrawSurface;

/**
 * The type High scores animation.
 */
public class HighScoresAnimation implements Animation {
    private HighScoreManager highScoreManager;

    /**
     * Instantiates a new High scores animation.
     *
     * @param manager the highscoremanager
     */
    public HighScoresAnimation(HighScoreManager manager) {
        highScoreManager = manager;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        d.drawText(d.getWidth() / 2, d.getHeight() / 7, "Your high score is: ", 32);
        d.drawText(d.getWidth() / 2, d.getHeight() / 4, Integer.toString(highScoreManager.getHighScore()), 32);
    }

    @Override
    public boolean shouldStop() {
        return false;
    }

    @Override
    public void forceContinue() {

    }
}
