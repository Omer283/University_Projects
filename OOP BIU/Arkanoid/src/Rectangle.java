/**
 * The type Rectangle.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class Rectangle {
    private Point upperLeft, bottomRight;
    private double width, height;
    private double[] xVals, yVals;

    /**
     * Instantiates a new Rectangle.
     *
     * @param upLeft the upper left point
     * @param w      the width
     * @param h      the height
     */
    public Rectangle(Point upLeft, double w, double h) {
        this.width = w;
        this.height = h;
        this.upperLeft = upLeft;
        this.bottomRight = new Point(upLeft.getX() + w, upLeft.getY() + h);
        xVals = new double[2]; //represent the xvals and yvals of rectangle (since it's parllel to axes)
        yVals = new double[2];
        xVals[0] = upperLeft.getX();
        xVals[1] = bottomRight.getX();
        yVals[0] = upperLeft.getY();
        yVals[1] = bottomRight.getY();
    }

    /**
     * Finds intersection points of line and rectangle.
     * @param line the line
     * @return list of all intersection points
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        java.util.List<Point> l = new java.util.ArrayList<Point>();
        Point tmpPoint;
        int[][] adder = {{0, 0, 1, 0}, {0, 0, 0, 1}, {1, 0, 1, 1}, {0, 1, 1, 1}};
        boolean repeatedFlag = false;
        for (int i = 0; i < 4; i++) { //checks each of the lines
            tmpPoint = line.intersectionWith(new Line(xVals[adder[i][0]], yVals[adder[i][1]], xVals[adder[i][2]],
                    yVals[adder[i][3]]));
            if (tmpPoint != null) {
                repeatedFlag = false;
                for (Point x : l) {
                    if (x.equals(tmpPoint)) { //checks if we added this point already
                        repeatedFlag = true;
                        break;
                    }
                }
                if (!repeatedFlag) {
                    l.add(tmpPoint);
                }
            }
        }
        return l;
    }

    /**
     * Gets height of rectangle.
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets width of rectangle.
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets upper left point of rectangle.
     * @return the upper left
     */
    public Point getUpperLeft() {
        return upperLeft;
    }

    /**
     * Gets bottom right point of rectangle.
     * @return the bottom right
     */
    public Point getBottomRight() {
        return bottomRight;
    }

    /**
     * Moves the rectangle by a certain distance horizontally.
     * @param dist the distance
     */
    public void moveHorizontally(int dist) {
        this.upperLeft.setX(this.upperLeft.getX() + dist);
        this.bottomRight.setX(this.bottomRight.getX() + dist);
        xVals[0] += dist;
        xVals[1] += dist;
    }

    /**
     * Gets xvals array.
     * @return the array
     */
    public double[] getxVals() {
        return xVals;
    }

    /**
     * Gets yvals array.
     * @return the array
     */
    public double[] getyVals() {
        return yVals;
    }

    /**
     * Center of mass point.
     * @return the center of mass
     */
    public Point centerOfMass() {
        return new Point((xVals[0] + xVals[1]) / 2, (yVals[0] + yVals[1]) / 2);
    }
}
