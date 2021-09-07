import biuoop.DrawSurface;

import java.awt.Color;
import java.awt.Image;

/**
 * The type Image block background.
 */
public class ImageBlockBackground implements BlockBackground {

    private Color stroke;
    private Rectangle rect;
    private Image image;

    /**
     * Instantiates a new Image block background.
     *
     * @param fillImg   the fill image
     * @param strokeCol the stroke color
     * @param rectangle the rectangle
     */
    public ImageBlockBackground(Image fillImg, Color strokeCol, Rectangle rectangle) {
        rect = rectangle;
        image = fillImg;
        stroke = strokeCol;
    }

    @Override
    public void draw(DrawSurface d) {
        d.drawImage((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(), image);
        if (stroke != null) {
            d.setColor(stroke);
            d.fillRectangle((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(), (int) rect.getWidth(),
                    (int) rect.getHeight());
            d.setColor(stroke);
            d.drawRectangle((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(), (int) rect.getWidth(),
                    (int) rect.getHeight());
        }
    }
}
