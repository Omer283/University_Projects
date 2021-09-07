import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Level 3.
 */
public class Level3 implements LevelInformation {

    /**
     * Instantiates a new Level 3.
     */
    public Level3() {
    }
    @Override
    public int numberOfBalls() {
        return 2;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> l = new ArrayList<>();
        l.add(new Velocity(-4, -4));
        l.add(new Velocity(4, -4));
        return l;
    }

    @Override
    public List<Point> initialBallPositions() {
        List<Point> l = new ArrayList<>();
        l.add(new Point(300,  400));
        l.add(new Point(500, 400));
        return l;
    }


    @Override
    public int paddleSpeed() {
        return 8;
    }

    @Override
    public int paddleWidth() {
        return 100;
    }

    @Override
    public String levelName() {
        return "Green 3";
    }

    @Override
    public Sprite getBackground() {
        return BackgroundFactory.getBackground(3);
    }

    @Override
    public List<Block> blocks() {
        List<Block> blockList = BackgroundFactory.getBackgroundBlocks();
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= 6 + i; j++) {
                java.awt.Color color;
                switch (i) {
                    case 0:
                        color = java.awt.Color.BLUE;
                        break;
                    case 1:
                        color = java.awt.Color.RED;
                        break;
                    case 2:
                        color = java.awt.Color.GREEN;
                        break;
                    case 3:
                        color = java.awt.Color.YELLOW;
                        break;
                    case 4:
                        color = java.awt.Color.ORANGE;
                        break;
                    case 5:
                        color = Color.pink;
                        break;
                    default:
                        color = java.awt.Color.red;
                        break;
                }
                Block block = new Block(new Rectangle(
                        new Point(GameLevel.WIDTH - 20 - 50 * j, GameLevel.HEIGHT / 3 - 20 * i), 50, 20), color);
                blockList.add(block);
            }
        }
        return blockList;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return 40;
    }
}
