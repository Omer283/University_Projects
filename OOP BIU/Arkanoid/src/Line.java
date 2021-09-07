/**
 * The type Line.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class Line {
    private Point start, end;

    /**
     * Instantiates a new Line.
     *
     * @param start the start point
     * @param end   the end point
     */
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Instantiates a new Line.
     *
     * @param x1 the x of start point
     * @param y1 the y of start point
     * @param x2 the x of end point
     * @param y2 the y of end point
     */
    public Line(double x1, double y1, double x2, double y2) {
        start = new Point(x1, y1);
        end = new Point(x2, y2);
    }

    /**
     * Length of line segment.
     *
     * @return the length
     */
    public double length() {
        return start.distance(end);
    }

    /**
     * Finds slope.
     *
     * @return the slope, or null if startX == endX
     */
    public Double slope() {
        if (start.getX() == end.getX()) {
            return null; //line will be of the form x = b
        }
        return (start.getY() - end.getY()) / (start.getX() - end.getX()); //known formula
    }

    /**
     * finds the b in the line equation.
     *
     * @return the b when line is represented as y = m * x + b, or x = b
     */
    public double offset() {
        Double m = slope();
        if (m == null) {
            return start.getX(); //line is x = b
        } else {
            return start.getY() - slope() * start.getX(); //y - mx = b
        }
    }

    /**
     * Middle point.
     *
     * @return the middle point
     */
    public Point middle() {
        return new Point((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
        //known formula
    }

    /**
     * Start point.
     *
     * @return the start point
     */
    public Point start() {
        return start;
    }

    /**
     * End point.
     *
     * @return the end point
     */
    public Point end() {
        return end;
    }

    /**
     * Checks if a point is on a line segment.
     *
     * @param pt the point
     * @return true iff the point is on the line segment (including ends)
     */
    public boolean isTouching(Point pt) {
        if (start.getX() != end.getX()) {
            double t = (pt.getX() - end.getX()) / (start.getX() - end.getX());
            //we represent line segment as a * t + b * (1 - t) for 0 <= t <= 1
            if (t < -Point.EPSILON || t > 1 + Point.EPSILON) { //check if it lands outside of the segment
                return false;
            }
            return (Math.abs(start.getY() * t + end.getY() * (1 - t) - pt.getY()) <= Point.EPSILON);
            //true iff the y values collide (or at least close enough, to avoid precision errors)
        } else {
            double max = Math.max(start.getY(), end.getY()), min = Math.min(start.getY(), end.getY());
            return (Math.abs(pt.getX() - start.getX()) <= Point.EPSILON && pt.getY() >= min - Point.EPSILON
                    && pt.getY() <= max + Point.EPSILON); //checks if point is on the same x axis, and in between
        }
    }

    /**
     * Checks if a line is a point.
     *
     * @return true iff start == end
     */
    public boolean isPoint() {
        return start.equals(end);
    }

    /**
     * Checks if line intersects with other line.
     *
     * @param other the other line
     * @return true iff the lines intersect
     */
    public boolean isIntersecting(Line other) {
        Double m1 = slope(), m2 = other.slope();
        double b1 = offset(), b2 = other.offset();
        double supposedY, supposedX;
        if (isPoint() && other.isPoint()) { //if both are points
            return start.equals(other.start());
        } else if (isPoint()) { //if one is a point
            return other.isTouching(start);
        } else if (other.isPoint()) { //if other is a point
            return isTouching(other.start());
        } else if ((m1 == null && m2 == null) || (m1 != null && m2 != null && m1.doubleValue() == m2.doubleValue())) {
            //slopes are equal
            if (b1 != b2) {
                return false; //offsets aren't so parallel lines
            }
            if (start.equals(other.start())) {
                return !isTouching(other.end()) && !other.isTouching(end); //checks if no other point is in both lines
            } else if (start.equals(other.end())) {
                return !isTouching(other.start()) && !other.isTouching(end);
            } else if (end.equals(other.start())) {
                return !isTouching(other.end()) && !other.isTouching(start);
            } else if (end.equals(other.end())) {
                return !isTouching(other.start()) && !other.isTouching(start);
            } else {
                return false;
            }
        } else if (m1 == null) { //one line is parallel to x axis
            supposedY = m2 * b1 + b2;
            Point p = new Point(b1, supposedY);
            return other.isTouching(p) && isTouching(p);
        } else if (m2 == null) {
            supposedY = m1 * b2 + b1;
            Point p = new Point(b2, supposedY);
            return isTouching(p) && other.isTouching(p);
        } else { //m1 != m2 and both arent parallel to x axis
            supposedX = (b2 - b1) / (m1 - m2);
            supposedY = m1 * supposedX + b1;
            Point p = new Point(supposedX, supposedY);
            return isTouching(p) && other.isTouching(p);
        }
    }

    /**
     * Finds intersection with other line.
     *
     * @param other the other line
     * @return the point (if exists) or null of doesn't exist
     */
    public Point intersectionWith(Line other) {
        if (!isIntersecting(other)) {
            return null;
        }
        //pretty much repeats isIntersecting
        Double m1 = slope(), m2 = other.slope(), b1 = offset(), b2 = other.offset();
        double supposedX, supposedY;
        if ((m1 == null && m2 == null) || (m1 != null && m2 != null && m1.doubleValue() == m2.doubleValue())) {
            if (isTouching(other.start())) {
                return other.start();
            } else if (isTouching(other.end())) {
                return other.end();
            } else if (other.isTouching(start)) {
                return start;
            } else {
                return end; //has to be one of those, since we know they intersect
            }
        } else if (m1 == null) {
        supposedY = m2 * b1 + b2;
        return new Point(b1, supposedY);
        } else if (m2 == null) {
        supposedY = m1 * b2 + b1;
        return new Point(b2, supposedY);
        } else { //m1 != m2 and both arent parallel to x axis
        supposedX = (b2 - b1) / (m1 - m2);
        supposedY = m1 * supposedX + b1;
        Point p = new Point(supposedX, supposedY);
        return p;
        }
    }

    /**
     * does equal other line.
     *
     * @param other the other line
     * @return true iff (start == start) && (end == end)
     */
    public boolean equals(Line other) {
        return (end.equals(other.end()) && start.equals(other.start()));
    }

    /**
     * Finds closest intersection of a line & rectangle from start of line.
     *
     * @param rect the rectangle
     * @return the intersection (or null if nonexistent)
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        //ALLOWED?
        java.util.List<Point> intersections = (java.util.ArrayList<Point>) rect.intersectionPoints(this);
        if (intersections.isEmpty()) {
            return null;
        }
        Point bestPoint = null;
        double minDistance = Double.MAX_VALUE; //MULTIPLE CLOSEST
        for (Point p : intersections) {
            if (this.start().distance(p) < minDistance) {
                bestPoint = p;
                minDistance = this.start().distance(p);
            }
        }
        return bestPoint;
    }
}