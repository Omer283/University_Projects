import java.awt.Color;

/**
 * The type Colors parser.
 */
public class ColorsParser {
    /**
     * Color from string java . awt . color.
     *
     * @param s the string
     * @return the color
     */
// parse color definition and return the specified color.
    public static java.awt.Color colorFromString(String s) {
        if (s == null) {
            return null;
        } else if (s.startsWith("RGB")) {
            String sub = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
            String[] values = sub.split(",");
            return new Color(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
        } else {
            switch (s) {
                case "black":
                    return Color.BLACK;
                case "blue":
                    return Color.BLUE;
                case "cyan":
                    return Color.CYAN;
                case "gray":
                    return Color.GRAY;
                case "lightGray":
                    return Color.LIGHT_GRAY;
                case "green":
                    return Color.GREEN;
                case "orange":
                    return Color.ORANGE;
                case "pink":
                    return Color.PINK;
                case "red":
                    return Color.RED;
                case "white":
                    return Color.WHITE;
                case "yellow":
                    return Color.YELLOW;
                default:
                    return null;
            }
        }
    }
}
