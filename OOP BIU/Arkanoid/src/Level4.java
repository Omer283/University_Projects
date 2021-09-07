import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Level 4.
 */
public class Level4 implements LevelInformation {
    @Override
    public int numberOfBalls() {
        return 3;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> l = new ArrayList<>();
        l.add(new Velocity(-4, -4));
        l.add(new Velocity(0, -7));
        l.add(new Velocity(4, -4));
        return l;
    }

    @Override
    public List<Point> initialBallPositions() {
        List<Point> l = new ArrayList<>();
        l.add(new Point(300,  400));
        l.add(new Point(400, 300));
        l.add(new Point(500, 400));
        return l;
    }

    @Override
    public int paddleSpeed() {
        return 15;
    }

    @Override
    public int paddleWidth() {
        return 70;
    }

    @Override
    public String levelName() {
        return "Final Four";
    }

    @Override
    public Sprite getBackground() {
        return BackgroundFactory.getBackground(4);
    }

    @Override
    public List<Block> blocks() {
        List<Block> blockList = BackgroundFactory.getBackgroundBlocks();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 19; j++) {
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
                Block b = new Block(new Rectangle(new Point(20 + 40 * j, 50 + 20 * i), 40, 20), color);
                blockList.add(b);
            }
        }
        return blockList;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return 133;
    }
}
