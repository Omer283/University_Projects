/**
 * The type Velocity.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class Velocity {
    private double dx, dy;

    /**
     * Instantiates a new Velocity.
     *
     * @param dx the velocity in x direction
     * @param dy the velocity in y direction
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Apply the speed to the point.
     *
     * @param p the old point
     * @return the new point after speed change
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + dx, p.getY() + dy);
    }

    /**
     * Converts (angle, speed) -> (dx, dy).
     *
     * @param angle the angle
     * @param speed the speed
     * @return the velocity in terms of x direction and y direction
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double angleInRadians = Math.PI * angle / 180;
        double dx = speed * Math.sin(angleInRadians); //trig
        double dy = -speed * Math.cos(angleInRadians);
        return new Velocity(dx, dy);
    }

    /**
     * Gets dx.
     * @return the dx
     */
    public double getDx() {
        return dx;
    }

    /**
     * Gets dy.
     *
     * @return the dy
     */
    public double getDy() {
        return dy;
    }

    /**
     * Sets dx.
     *
     * @param dxVal the dx val
     */
    public void setDx(double dxVal) {
        dx = dxVal;
    }

    /**
     * Sets dy.
     *
     * @param dyVal the dy val
     */
    public void setDy(double dyVal) {
        dy = dyVal;
    }

    /**
     * Gets size of velocity (absolute value in R2).
     *
     * @return the size / norm / absolute value
     */
    public double getSize() {
        return Math.sqrt(dx * dx + dy * dy);
    }
}
