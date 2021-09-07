/**
 * The type Point.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class Point {
    private double x, y;
    public static final double EPSILON = 0.0001;
    /**
     * Instantiates a new Point.
     *
     * @param x the x value
     * @param y the y value
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Distance between two points.
     *
     * @param other the other point
     * @return the distance
     */
    public double distance(Point other) {
        return Math.sqrt((x - other.getX()) * (x - other.getX()) + (y - other.getY()) * (y - other.getY()));
    }

    /**
     * Check if point equals to other point.
     *
     * @param other the other point
     * @return true iff |x1 - x2| <= epsilon && |y1 - y2| <= epsilon (to avoid precision errors)
     */
    public boolean equals(Point other) {
        return (Math.abs(this.x - other.getX()) <= Point.EPSILON
                && Math.abs(this.y - other.getY()) <= Point.EPSILON); //epsilon to avoid precision errors
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Sets x.
     *
     * @param xVal the x val
     */
    public void setX(double xVal) {
        x = xVal;
    }

    /**
     * Sets y.
     *
     * @param yVal the y val
     */
    public void setY(double yVal) {
        y = yVal;
    }
}
