import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * The type Image block factory.
 */
public class ImageBlockFactory implements BlockCreator {

    private int width, height;
    private Color borderCol;
    private Image image;

    /**
     * Instantiates a new Image block factory.
     *
     * @param w         the width
     * @param h         the height
     * @param imageStr  the image str path
     * @param strokeCol the stroke color
     */
    public ImageBlockFactory(Integer w, Integer h, String imageStr, String strokeCol) {
        width = w;
        height = h;
        borderCol = ColorsParser.colorFromString(strokeCol);
        try {
            image = ImageIO.read(new File("resources\\" + imageStr));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Block create(int xpos, int ypos) {
        Rectangle rect = new Rectangle(new Point(xpos, ypos), width, height);
        BlockBackground bg = new ImageBlockBackground(image, borderCol, rect);
        return new Block(rect, bg);
    }
}
