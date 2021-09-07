/**
 * The type Color block factory.
 */
public class ColorBlockFactory implements BlockCreator {

    private int width, height;
    private java.awt.Color fill, stroke;

    /**
     * Instantiates a new Color block factory.
     *
     * @param w         the w
     * @param h         the h
     * @param fillStr   the fill str
     * @param strokeStr the stroke str
     */
    public ColorBlockFactory(Integer w, Integer h, String fillStr, String strokeStr) {
        width = w;
        height = h;
        fill = ColorsParser.colorFromString(fillStr);
        stroke = ColorsParser.colorFromString(strokeStr);
    }

    @Override
    public Block create(int xpos, int ypos) {
        Rectangle rect = new Rectangle(new Point(xpos, ypos), width, height);
        BlockBackground bg = new ColorBlockBackground(fill, stroke, rect);
        return new Block(rect, bg);
    }
}
