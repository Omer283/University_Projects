
import biuoop.DrawSurface;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Background factory.
 */
public class BackgroundFactory {
    /**
     * Gets a background per request.
     *
     * @param level the level id
     * @return the background
     */
    public static Sprite getBackground(int level) {
        switch (level) {
            case 1:
                return new Background1();
            case 2:
                return new Background2();
            case 3:
                return new Background3();
            case 4:
                return new Background4();
            default:
                return null;
        }
    }

    /**
     * Gets background blocks.
     *
     * @return the background blocks
     */
    public static List<Block> getBackgroundBlocks() {
        List<Block> blockList = new ArrayList<>();
        Block[] borders = {new Block(new Rectangle(new Point(0, 25), 25, GameLevel.HEIGHT - 25),
                java.awt.Color.GRAY),
                new Block(new Rectangle(new Point(0, 0), GameLevel.WIDTH, 25),
                        java.awt.Color.GRAY),
                new Block(new Rectangle(new Point(GameLevel.WIDTH - 25, 25), 25, GameLevel.HEIGHT - 20),
                        java.awt.Color.GRAY),
                new Block(new Rectangle(new Point(25, GameLevel.HEIGHT - 25), GameLevel.WIDTH - 50, 25),
                        java.awt.Color.GRAY)};
        for (int r = 0; r < 4; r++) {
            if (r == 3) {
                borders[r].setKiller(); //sets a killer border
            }
            borders[r].setBorder();
            blockList.add(borders[r]);
        }
        return blockList;
    }

    /**
     * The First background.
     */
    private static class Background1 implements Sprite {
        @Override
        public void drawOn(DrawSurface d) {
            d.setColor(Color.darkGray);
            d.fillRectangle(0, 0,  GameLevel.WIDTH, GameLevel.HEIGHT);
        }

        @Override
        public void timePassed() {

        }
    }

    /**
     * The Second background.
     */
    public static class Background2 implements Sprite {
        @Override
        public void drawOn(DrawSurface d) {
            d.setColor(Color.pink);
            d.fillRectangle(0, 0,  GameLevel.WIDTH, GameLevel.HEIGHT);
            d.setColor(Color.red);
            d.fillCircle(225, 200, 50);
            d.fillCircle(425, 200, 50);
            d.fillRectangle(575, 150, 20, 100);
            d.fillCircle(600, 175, 25); //oop <3
        }

        @Override
        public void timePassed() {

        }
    }
    /**
     * The Third background.
     */
    private static class Background3 implements Sprite {

        @Override
        public void drawOn(DrawSurface d) {
            d.setColor(Color.PINK);
            d.fillRectangle(0, 0,  GameLevel.WIDTH, GameLevel.HEIGHT);
        }

        @Override
        public void timePassed() {

        }
    }

    /**
     * The Fourth background.
     */
    private static class Background4 implements Sprite {
        @Override
        public void drawOn(DrawSurface d) {

        }

        @Override
        public void timePassed() {

        }
    }

}
