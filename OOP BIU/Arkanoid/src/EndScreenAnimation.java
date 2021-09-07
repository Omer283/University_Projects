import biuoop.DrawSurface;

/**
 * The type End screen animation.
 */
public class EndScreenAnimation implements Animation {
    private boolean hasWon;
    private Counter score;

    /**
     * Instantiates a new End screen animation.
     *
     * @param winStatus    the win status
     * @param scoreCounter the score counter
     */
    public EndScreenAnimation(boolean winStatus, Counter scoreCounter) {
        score = scoreCounter;
        hasWon = winStatus;
    }


    @Override
    public void doOneFrame(DrawSurface d) {
        String message;
        if (hasWon) {
            message = "You win! ";
        } else {
            message = "Game Over!";
        }
        message += "Your score is " + score.getValue();
        d.drawText(10, d.getHeight() / 2, message, 32);
    }

    @Override
    public boolean shouldStop() {
        return false;
    }

    @Override
    public void forceContinue() {

    }
}