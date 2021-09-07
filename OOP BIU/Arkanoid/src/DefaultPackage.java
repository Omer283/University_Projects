/**
 * The type Default package.
 */
public class DefaultPackage {
    private Integer height = null, width = null;
    private String stroke = null, fill = null;

    /**
     * Instantiates a new Default package.
     *
     * @param s the string
     */
    public DefaultPackage(String s) {
        String[] splitted = s.split("\\s+");
        for (int i = 1; i < splitted.length; i++) { //check if each exist
            if (splitted[i].startsWith("height")) {
                height = Integer.parseInt(splitted[i].split(":")[1]);
            } else if (splitted[i].startsWith("width")) {
                width  = Integer.parseInt(splitted[i].split(":")[1]);
            } else if (splitted[i].startsWith("stroke")) {
                stroke = splitted[i].split(":")[1];
            } else if (splitted[i].startsWith("fill")) {
                fill = splitted[i].split(":")[1];
            }
        }
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Gets fill.
     *
     * @return the fill
     */
    public String getFill() {
        return fill;
    }

    /**
     * Gets stroke.
     *
     * @return the stroke
     */
    public String getStroke() {
        return stroke;
    }
}
