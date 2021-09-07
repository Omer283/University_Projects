import biuoop.DrawSurface;

/**
 * The type Score indicator.
 */
public class ScoreIndicator implements Sprite, GameObject {
    private Counter scoreCounter;
    private String levelName;

    /**
     * Instantiates a new Score indicator.
     *
     * @param c the counter
     * @param name the level name
     */
    public ScoreIndicator(Counter c, String name) {
        scoreCounter = c; levelName = name;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.drawText(350, 15, "Score: " + scoreCounter.getValue(),  15);
        d.drawText(550, 15, "Level name: " + levelName, 15);
    }

    @Override
    public void timePassed() {

    }

    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }

    @Override
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
    }
}
