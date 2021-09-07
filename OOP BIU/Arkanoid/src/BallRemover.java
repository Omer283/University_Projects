/**
 * The type BallRemover.
 */
public class BallRemover implements HitListener {
    private GameLevel g;
    private Counter counter;

    /**
     * Instantiates a new Ball remover.
     *
     * @param game the game
     * @param c    the counter
     */
    public BallRemover(GameLevel game, Counter c) {
        g = game;
        counter = c;
    }
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(g);
        counter.decrease(1);
    }
}
