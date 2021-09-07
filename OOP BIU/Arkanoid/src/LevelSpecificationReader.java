import biuoop.DrawSurface;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Level specification reader.
 */
public class LevelSpecificationReader {
    private boolean readAllLevels = false;

    /**
     * Instantiates a new Level specification reader.
     */
    public LevelSpecificationReader() {
    }

    /**
     * From reader list.
     *
     * @param reader the reader
     * @return the list
     * @throws IOException the io exception
     */
    public List<LevelInformation> fromReader(java.io.Reader reader) throws IOException {
        List<LevelInformation> l = new ArrayList<>();
        BufferedReader buf = new BufferedReader(reader);
        while (!readAllLevels) {
            LevelInformation info = readLevel(buf);
            l.add(info);
        }
        return l;
    }

    /**
     * Reads level.
     * @param buf reader
     * @return level
     * @throws IOException exception
     */
    private LevelInformation readLevel(BufferedReader buf) throws IOException {
        String line = "";
        boolean started = false;
        LevelFileInformation lfi = new LevelFileInformation();
        while (!line.equals("END_LEVEL")) {
            line = buf.readLine();
            if (line == null || line.equals("END_LEVEL")) {
                break;
            }
            if (line.length() == 0) {
                continue;
            } else if (line.equals("START_LEVEL")) {
                started = true;
            } else if (line.startsWith("level_name")) {
                lfi.setLevelName(line.split(":")[1]);
            } else if (line.startsWith("ball_velocities")) {
                List<Velocity> vels = new ArrayList<>();
                String[] velStrings = line.split(":")[1].split("\\s+");
                for (String s : velStrings) {
                    String[] coords = s.split(",");
                    vels.add(Velocity.fromAngleAndSpeed(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
                }
                lfi.setBallVel(vels);
            } else if (line.startsWith("background")) {
                lfi.setBackground(line.split(":")[1]);
            } else if (line.startsWith("paddle_speed")) {
                lfi.setpSpeed(Integer.parseInt(line.split(":")[1]));
            } else if (line.startsWith("paddle_width")) {
                lfi.setpWidth(Integer.parseInt(line.split(":")[1]));
            } else if (line.startsWith("block_definitions")) {
                lfi.setBlockDef(line.split(":")[1]);
            } else if (line.startsWith("blocks_start_x")) {
                lfi.setBlockX(Integer.parseInt(line.split(":")[1]));
            } else if (line.startsWith("blocks_start_y")) {
                lfi.setBlockY(Integer.parseInt(line.split(":")[1]));
            } else if (line.startsWith("row_height")) {
                lfi.setRowHeight(Integer.parseInt(line.split(":")[1]));
            } else if (line.startsWith("num_blocks")) {
                lfi.setNumBlocks(Integer.parseInt(line.split(":")[1]));
            } else if (line.equals("START_BLOCKS")) {
                List<String> blockRows = new ArrayList<>();
                while (!line.equals("END_BLOCKS")) {
                    line = buf.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    blockRows.add(line);
                }
                lfi.setBlocksList(blockRows);
            }
        }
        if (!lfi.isValidLevel() && started) {
            throw new MissingLevelDetailsException("Not enough level details");
        }
        if (line.equals("END_LEVEL")) {
            line = buf.readLine();
        }
        if (line == null) {
            readAllLevels = true;
        }
        String details = lfi.getBackground().substring(lfi.getBackground().indexOf('(') + 1,
                lfi.getBackground().length() - 1);
        Sprite bg = null;
        if (lfi.getBackground().startsWith("image")) {
            Image image = ImageIO.read(new File("resources\\" + details));
            bg = new Sprite() {
                @Override
                public void drawOn(DrawSurface d) {
                    d.drawImage(0, 0, image);
                }

                @Override
                public void timePassed() {

                }
            };
        } else if (lfi.getBackground().startsWith("color")) {
            Color color = ColorsParser.colorFromString(details);
            bg = new Sprite() {

                @Override
                public void drawOn(DrawSurface d) {
                    d.fillRectangle(0, 0, GameLevel.WIDTH, GameLevel.HEIGHT);
                }

                @Override
                public void timePassed() {

                }
            };
        }
        List<Block> blocks = lfi.interpretBlocks();
        for (Block extra : BackgroundFactory.getBackgroundBlocks()) {
            blocks.add(extra);
        }
        return generateLevelInfo(lfi, bg, blocks);
    }

    /**
     * generate level info.
     * @param lfi yes
     * @param bg background
     * @param blocks blocks
     * @return level
     */
    private LevelInformation generateLevelInfo(LevelFileInformation lfi, Sprite bg, List<Block> blocks) {
        return new LevelInformation() {
            @Override
            public int numberOfBalls() {
                return lfi.getBallVel().size();
            }

            @Override
            public List<Velocity> initialBallVelocities() {
                return lfi.getBallVel();
            }

            @Override
            public List<Point> initialBallPositions() {
                List<Point> l = new ArrayList<>();
                int amt = numberOfBalls();
                for (int i = 0; i < amt; i++) {
                    l.add(new Point(GameLevel.WIDTH / 2 + 20 * (i - amt / 2), (GameLevel.HEIGHT * 2) / 3));
                }
                return l;
            }

            @Override
            public int paddleSpeed() {
                return lfi.getpSpeed();
            }

            @Override
            public int paddleWidth() {
                return lfi.getpWidth();
            }

            @Override
            public String levelName() {
                return lfi.getLevelName();
            }

            @Override
            public Sprite getBackground() {
                try {
                    return lfi.interpretBackground();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public List<Block> blocks() {
                return blocks;
            }

            @Override
            public int numberOfBlocksToRemove() {
                return lfi.getNumBlocks();
            }
        };
    }


}
