import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Level 2.
 */
public class Level2 implements LevelInformation {
    /**
     * Instantiates a new Level 2.
     */
    public Level2() {

    }
    @Override
    public int numberOfBalls() {
        return 2;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> l = new ArrayList<>();
        l.add(new Velocity(5, -3));
        l.add(new Velocity(-5, -3));
        return l;
    }

    @Override
    public List<Point> initialBallPositions() {
        List<Point> l = new ArrayList<>();
        l.add(new Point(400, 400));
        l.add(new Point(400, 400));
        return l;
    }

    @Override
    public int paddleSpeed() {
        return 5;
    }

    @Override
    public int paddleWidth() {
        return 600;
    }

    @Override
    public String levelName() {
        return "Wide Easy";
    }

    @Override
    public Sprite getBackground() {
        return BackgroundFactory.getBackground(2);
    }

    @Override
    public List<Block> blocks() {
        List<Block> blockList = BackgroundFactory.getBackgroundBlocks();
        double width = (double) 760 / 15;
        for (int i = 0; i < 15; i++) {
            int real = i - (i >= 7 ? 1 : 0);
            real >>= 1;
            java.awt.Color color;
            switch (real) {
                case 0:
                    color = java.awt.Color.RED;
                    break;
                case 1:
                    color = Color.orange;
                    break;
                case 2:
                    color = java.awt.Color.YELLOW;
                    break;
                case 3:
                    color = Color.GREEN;
                    break;
                case 4:
                    color = Color.BLUE;
                    break;
                case 5:
                    color = Color.pink;
                    break;
                case 6:
                    color = Color.CYAN;
                    break;
                default:
                    color = Color.RED;
                    break;
            }
            Point p = new Point(20 + width * i, 300);
            Block b = new Block(new Rectangle(p, width, 20), color);
            blockList.add(b);
        }
        return blockList;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return 15;
    }
}
