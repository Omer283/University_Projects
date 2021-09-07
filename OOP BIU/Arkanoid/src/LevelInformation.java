

import java.util.List;

/**
 * The interface Level information.
 */
public interface LevelInformation {
    /**
     * Number of balls.
     * @return the int
     */
    int numberOfBalls();

    /**
     * Initial ball velocities list.
     * @return the list
     */
    List<Velocity> initialBallVelocities();

    /**
     * Initial ball positions list.
     *
     * @return the list
     */
    List<Point> initialBallPositions();

    /**
     * Paddle speed.
     *
     * @return the int
     */
    int paddleSpeed();

    /**
     * Paddle width.
     *
     * @return the int
     */
    int paddleWidth();

    /**
     * Level name.
     *
     * @return the string
     */
    String levelName();

    /**
     * Gets background.
     *
     * @return the background
     */
    Sprite getBackground();

    /**
     * List of blocks.
     *
     * @return the list
     */
    List<Block> blocks();

    /**
     * Number of blocks to remove.
     *
     * @return the int
     */
    int numberOfBlocksToRemove();
}
