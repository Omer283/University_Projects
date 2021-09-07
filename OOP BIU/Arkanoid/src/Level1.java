import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Level 1.
 */
public class Level1 implements LevelInformation {

    /**
     * Instantiates a new Level 1.
     */
    public Level1() {
    }
    @Override
    public int numberOfBalls() {
        return 1;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> l = new ArrayList<>();
        l.add(new Velocity(0, -5));
        return l;
    }

    @Override
    public List<Point> initialBallPositions() {
        List<Point> l = new ArrayList<>();
        l.add(new Point(GameLevel.WIDTH / 2, 400));
        return l;
    }

    @Override
    public int paddleSpeed() {
        return 20;
    }

    @Override
    public int paddleWidth() {
        return 100;
    }

    @Override
    public String levelName() {
        return "Direct Hit";
    }

    @Override
    public Sprite getBackground() {
        return BackgroundFactory.getBackground(1);
    }

    @Override
    public List<Block> blocks() {
        List<Block> blockList = BackgroundFactory.getBackgroundBlocks();
        blockList.add(new Block(new Rectangle(new Point(350, 150),  100, 100), Color.RED));
        return blockList;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return 1;
    }
}