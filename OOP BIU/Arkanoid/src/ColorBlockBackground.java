import biuoop.DrawSurface;

import java.awt.Color;

/**
 * The type Color block background.
 */
public class ColorBlockBackground implements BlockBackground {
    private Color fill, stroke;
    private Rectangle rect;

    /**
     * Instantiates a new Color block background.
     *
     * @param fillCol   the fill color
     * @param strokeCol the stroke color
     * @param rectangle the rectangle
     */
    public ColorBlockBackground(Color fillCol, Color strokeCol, Rectangle rectangle) {
        rect = rectangle;
        fill = fillCol;
        stroke = strokeCol;
    }

    @Override
    public void draw(DrawSurface d) {
        if (fill != null) {
            d.setColor(fill);
            d.fillRectangle((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(), (int) rect.getWidth(),
                    (int) rect.getHeight());
        }
        if (stroke != null) {
            d.setColor(stroke);
            d.drawRectangle((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(), (int) rect.getWidth(),
                    (int) rect.getHeight());
        }
    }

    /**
     * Sets fill color.
     *
     * @param filler the filler
     */
    public void setFill(Color filler) {
        this.fill = filler;
    }

    /**
     * Sets stroke color.
     *
     * @param border the border
     */
    public void setStroke(Color border) {
        this.stroke = border;
    }
}
