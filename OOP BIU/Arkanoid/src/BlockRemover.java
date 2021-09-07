/**
 * The type BlockRemover.
 */
public class BlockRemover implements HitListener {
    private GameLevel g;
    private Counter remainingBlocks;

    /**
     * Instantiates a new Block remover.
     *
     * @param game          the game
     * @param removedBlocks the counter
     */
    public BlockRemover(GameLevel game, Counter removedBlocks) {
        g = game;
        remainingBlocks = removedBlocks;
    }


    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeFromGame(g);
        beingHit.removeHitListener(this);
        remainingBlocks.decrease(1);
    }
}