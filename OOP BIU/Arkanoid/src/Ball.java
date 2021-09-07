import java.awt.Color;

/**
 * The type Ball.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class Ball implements Sprite, GameObject {
    private Point point;
    private int radius;
    private java.awt.Color color;
    private Velocity velocity;
    private GameEnvironment gameEnvironment;

    /**
     * Instantiates a new Ball.
     *
     * @param point the point coordinates
     * @param r     the radius
     * @param color the color of the ball
     */
    public Ball(Point point, int r, java.awt.Color color) {
        this.point = point;
        this.radius = r;
        this.color = color;
    }

    /**
     * Instantiates a new Ball.
     *
     * @param x     the x coordinate of the ball
     * @param y     the y coordinate of the ball
     * @param r     the radius
     * @param color the color
     */
    public Ball(double x, double y, int r, java.awt.Color color) {
        this.point = new Point(x, y);
        this.radius = r;
        this.color = color;
    }

    /**
     * Instantiates a new Ball.
     *
     * @param p the p
     */
    public Ball(Point p) {
        this(p, 6, Color.yellow);
    }

    /**
     * Gets the x value of ball.
     *
     * @return the x
     */
    public int getX() {
        return (int) point.getX();
    }

    /**
     * Gets the y value of ball.
     *
     * @return the y
     */
    public int getY() {
        return (int) point.getY();
    }

    /**
     * Draws ball on surface.
     *
     * @param d the surface to be drawn on
     */
    public void drawOn(biuoop.DrawSurface d) {
        d.setColor(color);
        d.fillCircle(getX(), getY(), radius);
    }

    /**
     * Sets velocity.
     *
     * @param dx the velocity in x direction
     * @param dy the velocity in y direction
     */
    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy);
    }

    /**
     * Sets velocity.
     *
     * @param vel the vel
     */
    public void setVelocity(Velocity vel) {
        this.velocity = vel;
    }


    /**
     * Computes trajectory line.
     *
     * @return the line
     */
    public Line computeTrajectory() {
        Point p2 = velocity.applyToPoint(point);
        return new Line(point, p2);
    }

    /**
     * Move the position of the ball one step according to speeds.
     * Change speeds / make corrections if out of bounds.
     */
    public void moveOneStep() {
        Line traj = computeTrajectory();
        CollisionInfo collisionInfo = gameEnvironment.getClosestCollision(traj);
        Point prevPoint = point;
        if (collisionInfo != null) { //If there's a collision
            Point collisionPoint = collisionInfo.collisionPoint();
            Collidable c = collisionInfo.collisionObject();
            velocity = c.hit(this, collisionPoint, velocity);
            Double slope = traj.slope();
            if (slope != null && slope.doubleValue() != 0) { //trajectory isn't vertical or horizontal
                if (Math.abs(collisionPoint.getY()
                        - c.getCollisionRectangle().getUpperLeft().getY()) <= Point.EPSILON) { //hit from above
                    if (prevPoint.getY() + radius <= collisionPoint.getY()) { //If there's enough space to bring back
                        //the ball to the right path
                        point.setY(collisionPoint.getY() - radius - Point.EPSILON);
                        point.setX(-radius / slope + collisionPoint.getX());
                    } else {
                        point = prevPoint; //returns to previous point
                    }
                } else if (Math.abs(collisionPoint.getY()
                        - c.getCollisionRectangle().getBottomRight().getY()) <= Point.EPSILON) { //hit from below
                    if (collisionPoint.getY() + radius <= prevPoint.getY()) { //If there's enough space to bring back
                        //the ball to the right path
                        point.setY(collisionPoint.getY() + radius + Point.EPSILON);
                        point.setX(radius / slope + collisionPoint.getX());
                    } else {
                        point = prevPoint;
                    }
                } else if (Math.abs(collisionPoint.getX()
                        - c.getCollisionRectangle().getUpperLeft().getX()) <= Point.EPSILON) { //hit from left
                    if (prevPoint.getX() + radius <= collisionPoint.getX()) { //If there's enough space to bring back
                        //the ball to the right path
                        point.setX(collisionPoint.getX() - radius - Point.EPSILON);
                        point.setY(-radius / slope + collisionPoint.getY());
                    } else {
                        point = prevPoint;
                    }
                } else if (Math.abs(collisionPoint.getX()
                        - c.getCollisionRectangle().getBottomRight().getX()) <= Point.EPSILON) { //hit from right
                    if (collisionPoint.getX() + radius <= prevPoint.getX()) { //If there's enough space to bring back
                        //the ball to the right path
                        point.setX(collisionPoint.getX() + radius - Point.EPSILON);
                        point.setY(radius / slope + collisionPoint.getY());
                    } else {
                        point = prevPoint;
                    }
                }
            } else { //Corrections if the slope is vertical or horizontal
                if (Math.abs(collisionPoint.getX()
                        - c.getCollisionRectangle().getUpperLeft().getX()) <= Point.EPSILON) { //hit from left
                    point.setX(collisionPoint.getX() - radius);
                } else if (Math.abs(collisionPoint.getX()
                        - c.getCollisionRectangle().getBottomRight().getX()) <= Point.EPSILON) { //hit from right
                    point.setX(collisionPoint.getX() + radius);
                } else if (Math.abs(collisionPoint.getY()
                        - c.getCollisionRectangle().getUpperLeft().getY()) <= Point.EPSILON) { //hit from above
                    point.setY(collisionPoint.getY() - radius);
                } else if (Math.abs(collisionPoint.getY()
                        - c.getCollisionRectangle().getBottomRight().getY()) <= Point.EPSILON) { //hit from below
                    point.setY(collisionPoint.getY() + radius);
                }
            }
        } else { //No collision, so moves normally
            point = velocity.applyToPoint(point);
        }
    }

    /**
     * Sets game environment for the ball.
     *
     * @param envi the game environment
     */
    public void setGameEnvironment(GameEnvironment envi) {
        this.gameEnvironment = envi;
    }

    /**
     * Gets game environment of ball.
     *
     * @return the game environment
     */
    public GameEnvironment getGameEnvironment() {
        return gameEnvironment;
    }

    /**
     * Sets color of ball.
     *
     * @param col the color
     */
    public void setColor(java.awt.Color col) {
        this.color = col;
    }

    /**
     * Gets color of ball.
     *
     * @return the color
     */
    public java.awt.Color getColor() {
        return color;
    }

    /**
     * Updates the ball after a time unit has passed.
     */
    public void timePassed() {
        moveOneStep();
    }

    /**
     * Add the sprite / collidable to game.
     * @param g the game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }

    /**
     * Gets velocity of ball.
     *
     * @return the velocity
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * Gets radius of ball.
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    @Override
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
    }
}
