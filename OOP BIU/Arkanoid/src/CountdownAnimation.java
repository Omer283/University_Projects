import biuoop.DrawSurface;

/**
 * The type Countdown animation.
 */
public class CountdownAnimation implements Animation {
    private Counter counter;
    private SpriteCollection spriteCollection;
    private int decrement, perSecond;
    private Sprite background = null;
    private boolean stop = false;

    /**
     * Instantiates a new Countdown animation.
     *
     * @param numOfSeconds the num of seconds
     * @param countFrom    the number to count from
     * @param gameScreen   the game screen
     */
    public CountdownAnimation(double numOfSeconds, int countFrom, SpriteCollection gameScreen) {
        spriteCollection = gameScreen;
        counter = new Counter((int) (1000 * numOfSeconds)); //miliseconds
        decrement = 17; //each frame is approx 17 ms
        perSecond = (int) ((numOfSeconds * 1000) / countFrom); //each second in timer takes perSecond ms
    }
    @Override
    public void doOneFrame(DrawSurface d) {
        if (background != null) {
            background.drawOn(d); //draws bg
        }
        spriteCollection.drawAllOn(d);
        int number = currSeconds();
        d.drawText(d.getWidth() / 2, d.getHeight() / 2, Integer.toString(number), 32);
        counter.decrease(decrement);
        if (counter.getValue() <= 0) {
            stop = true;
        }
    }

    /**
     * Returns the number shown on timer.
     * @return the number
     */
    private int currSeconds() {
        return counter.getValue() / perSecond + 1;
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }

    @Override
    public void forceContinue() {
        stop = false;
    }

    /**
     * Sets background.
     *
     * @param bg the bg
     */
    public void setBackground(Sprite bg) {
        background = bg;
    }
}
