import biuoop.DrawSurface;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Level file information.
 */
public class LevelFileInformation {
    private String levelName, background, blockDef;
    private Integer pSpeed, pWidth, blockX, blockY, rowHeight, numBlocks;
    private List<Velocity> ballVel;
    private List<String> blocksList;

    /**
     * Instantiates a new Level file information.
     */
    public LevelFileInformation() {

    }

    /**
     * Sets background.
     *
     * @param background1 the background 1
     */
    public void setBackground(String background1) {
        this.background = background1;
    }

    /**
     * Sets ball vel.
     *
     * @param ballVelocities the ball velocities
     */
    public void setBallVel(List<Velocity> ballVelocities) {
        this.ballVel = ballVelocities;
    }

    /**
     * Sets block def.
     *
     * @param blockDefine the block define
     */
    public void setBlockDef(String blockDefine) {
        this.blockDef = blockDefine;
    }

    /**
     * Sets block x.
     *
     * @param blockXVal the block x val
     */
    public void setBlockX(Integer blockXVal) {
        this.blockX = blockXVal;
    }

    /**
     * Sets block y.
     *
     * @param blockYVal the block y val
     */
    public void setBlockY(Integer blockYVal) {
        this.blockY = blockYVal;
    }

    /**
     * Sets level name.
     *
     * @param name the name
     */
    public void setLevelName(String name) {
        this.levelName = name;
    }

    /**
     * Sets num blocks.
     *
     * @param blocks the blocks
     */
    public void setNumBlocks(Integer blocks) {
        this.numBlocks = blocks;
    }

    /**
     * Sets speed.
     *
     * @param speed the speed
     */
    public void setpSpeed(Integer speed) {
        this.pSpeed = speed;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setpWidth(Integer width) {
        this.pWidth = width;
    }

    /**
     * Sets row height.
     *
     * @param height the height
     */
    public void setRowHeight(Integer height) {
        this.rowHeight = height;
    }

    /**
     * Sets blocks list.
     *
     * @param list the list
     */
    public void setBlocksList(List<String> list) {
        this.blocksList = list;
    }

    /**
     * Is valid level.
     *
     * @return the boolean
     */
    public boolean isValidLevel() {
        return !(rowHeight == null || pWidth == null || pSpeed == null || numBlocks == null || levelName == null
                || blockY == null || blockX == null || blockDef == null || ballVel == null || background == null
                || blocksList == null);
    }

    /**
     * Interpret blocks list.
     *
     * @return the list of blocks
     */
    public List<Block> interpretBlocks() {
        List<Block> list = new ArrayList<>();
        BlocksFromSymbolsFactory factory;
        try {
            factory = new BlocksDefinitionReader().fromReader(new BufferedReader(new FileReader(
                    "resources\\" + blockDef)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int x = blockX, y = blockY, delta = rowHeight;
        for (String s : blocksList) {
            for (int i = 0; i < s.length(); i++) {
                String symbol = String.valueOf(s.charAt(i));
                if (factory.isBlockSymbol(symbol)) {
                    Block b = factory.getBlock(symbol, x, y);
                    x += b.getCollisionRectangle().getWidth(); //moves xval by width
                    list.add(b);
                } else if (factory.isSpaceSymbol(symbol)) {
                    x += factory.getSpaceWidth(symbol);
                }
            }
            x = blockX;
            y += delta; //moves yval by row height
        }
        return list;
    }

    /**
     * Interpret background.
     *
     * @return the sprite background
     * @throws IOException exception
     */
    public Sprite interpretBackground() throws IOException {
        String details = background.substring(background.indexOf('(') + 1, background.length() - 1);
        Sprite bg = null;
        if (background.startsWith("image")) { //image background
            Image image = ImageIO.read(new File("resources\\" + details));
            return new Sprite() {
                @Override
                public void drawOn(DrawSurface d) {
                    d.drawImage(0, 0, image);
                }

                @Override
                public void timePassed() {

                }
            };
        } else if (background.startsWith("color")) { //color background
            Color color = ColorsParser.colorFromString(details);
            return new Sprite() {

                @Override
                public void drawOn(DrawSurface d) {
                    d.setColor(color);
                    d.fillRectangle(0, 0, GameLevel.WIDTH, GameLevel.HEIGHT);
                }

                @Override
                public void timePassed() {

                }
            };
        }
        return null;
    }

    /**
     * Gets level name.
     *
     * @return the level name
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Gets block def.
     *
     * @return the block def
     */
    public String getBlockDef() {
        return blockDef;
    }

    /**
     * Gets background.
     *
     * @return the background
     */
    public String getBackground() {
        return background;
    }

    /**
     * Gets ball vel.
     *
     * @return the ball vel
     */
    public List<Velocity> getBallVel() {
        return ballVel;
    }

    /**
     * Gets blocks list.
     *
     * @return the blocks list
     */
    public List<String> getBlocksList() {
        return blocksList;
    }

    /**
     * Gets row height.
     *
     * @return the row height
     */
    public Integer getRowHeight() {
        return rowHeight;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public Integer getpWidth() {
        return pWidth;
    }

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public Integer getpSpeed() {
        return pSpeed;
    }

    /**
     * Gets num blocks.
     *
     * @return the num blocks
     */
    public Integer getNumBlocks() {
        return numBlocks;
    }

    /**
     * Gets block y.
     *
     * @return the block y
     */
    public Integer getBlockY() {
        return blockY;
    }

    /**
     * Gets block x.
     *
     * @return the block x
     */
    public Integer getBlockX() {
        return blockX;
    }

}
