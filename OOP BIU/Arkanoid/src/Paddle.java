/**
 * The type Paddle.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class Paddle implements Sprite, Collidable, GameObject {
    private biuoop.KeyboardSensor keyboard;
    private Rectangle rect;
    private java.awt.Color color;
    private int speed = 8, regions = 5, leftBorder, rightBorder, width;

    /**
     * Instantiates a new Paddle.
     */
    public Paddle() {
        this(100, 8);
    }

    /**
     * Instantiates a new Paddle.
     *
     * @param wide the wide
     */
    public Paddle(int wide) {
        this(wide, 8);
    }

    /**
     * Instantiates a new Paddle.
     *
     * @param wide the wide
     * @param vel  the vel
     */
    public Paddle(int wide, int vel) {
        width = wide;
        color = java.awt.Color.gray;
        rect = new Rectangle(new Point(GameLevel.WIDTH / 2 - width / 2, GameLevel.HEIGHT - 30), width, 10);
        speed = vel;
        leftBorder = 20;
        rightBorder = GameLevel.WIDTH - 20;
    }

    /**
     * Instantiates a new Paddle.
     *
     * @param rectangle the rectangle
     */
    public Paddle(Rectangle rectangle) {
        rect = rectangle;
        color = java.awt.Color.gray;
    }

    /**
     * Instantiates a new Paddle.
     *
     * @param rectangle the rectangle
     * @param col       the color
     */
    public Paddle(Rectangle rectangle, java.awt.Color col) {
        color = col;
        rect = rectangle;
    }

    /**
     * Instantiates a new Paddle.
     *
     * @param rectangle the rectangle
     * @param col       the color
     * @param sp        the speed
     */
    public Paddle(Rectangle rectangle, java.awt.Color col, int sp) {
        color = col;
        rect = rectangle;
        speed = sp;
    }

    /**
     * Moves left.
     */
    public void moveLeft() {
        rect.moveHorizontally((int) Math.max(-speed, Math.round(leftBorder - rect.getUpperLeft().getX())));
    }

    /**
     * Moves right.
     */
    public void moveRight() {
        rect.moveHorizontally((int) Math.min(speed, Math.round(rightBorder - rect.getBottomRight().getX())));
    }

    /**
     * When a frame passes, checks if arrow is pressed and calls move function.
     */
    public void timePassed() {
        if (keyboard.isPressed(biuoop.KeyboardSensor.LEFT_KEY) && rect.getUpperLeft().getX() > leftBorder) {
            moveLeft();
        } else if (keyboard.isPressed(biuoop.KeyboardSensor.RIGHT_KEY) && rect.getBottomRight().getX() < rightBorder) {
            moveRight();
        }
    }

    /**
     * Draws paddle on drawsurface.
     * @param d the drawsurface
     */
    public void drawOn(biuoop.DrawSurface d) {
        d.setColor(color);
        d.fillRectangle((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(), (int) rect.getWidth(),
                (int) rect.getHeight());
    }

    /**
     * Gets collision rectangle.
     * @return rectangle
     */
    public Rectangle getCollisionRectangle() {
        return rect;
    }

    /**
     * Notifies paddle if got hit by a ball, and returns desired ball speed after collision.
     * @param collisionPoint  the collision point
     * @param currentVelocity the current velocity of the ball
     * @param hitter the hitter ball
     * @return the future velocity of the ball
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double[] x = rect.getxVals(), y = rect.getyVals();
        if (collisionPoint.equals(new Point(x[0], y[0]))) { //checks if collision equals corner
            return new Velocity(-Math.abs(currentVelocity.getDx()), -Math.abs(currentVelocity.getDy()));
        } else if (collisionPoint.equals(new Point(x[1], y[0]))) {
            return new Velocity(Math.abs(currentVelocity.getDx()), -Math.abs(currentVelocity.getDy()));
        } else if (collisionPoint.equals(new Point(x[0], y[1]))) {
            return new Velocity(-Math.abs(currentVelocity.getDx()), Math.abs(currentVelocity.getDy()));
        } else if (collisionPoint.equals(new Point(x[1], y[1]))) {
            return new Velocity(Math.abs(currentVelocity.getDx()), Math.abs(currentVelocity.getDy()));
        } else if (Math.abs(collisionPoint.getY() - rect.getUpperLeft().getY()) <= Point.EPSILON
                || Math.abs(collisionPoint.getY() - rect.getBottomRight().getY()) <= Point.EPSILON) {
            //Hits the paddle from above/below
            if (regions <= 1) {
                return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
            }
            double regionSize = rect.getWidth() / regions, velSize = currentVelocity.getSize();
            for (int i = 1; i <= regions; i++) { //spooky math stuff
                if (collisionPoint.getX() - Point.EPSILON <= rect.getUpperLeft().getX() + regionSize * i) {
                    if (i != 3) {
                        Velocity tmpVelocity = Velocity.fromAngleAndSpeed(300
                                + (double) (120 * (i - 1)) / (regions - 1), velSize); //divides area [-60,60]
                        if (Math.abs(collisionPoint.getY() - rect.getBottomRight().getY()) <= Point.EPSILON) {
                            tmpVelocity.setDy(-tmpVelocity.getDy()); //if hit from below, reverse y axis
                        }
                        return tmpVelocity;
                    } else {
                        return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
                    }
                }
            }
            return Velocity.fromAngleAndSpeed(60, velSize); //for some reason, didn't get into any region
        } else if (Math.abs(collisionPoint.getX() - rect.getUpperLeft().getX()) <= Point.EPSILON
                || Math.abs(collisionPoint.getX() - rect.getBottomRight().getX()) <= Point.EPSILON) { //hit from side
            return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
        } else { //doesn't hit
            return currentVelocity;
        }
    }

    /**
     * Adds paddle to game collections.
     * @param g the game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    @Override
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
        g.removeCollidable(this);
    }

    /**
     * Sets color of paddle.
     *
     * @param col the color
     */
    public void setColor(java.awt.Color col) {
        this.color = col;
    }

    /**
     * Gets color of paddle.
     *
     * @return the color
     */
    public java.awt.Color getColor() {
        return color;
    }

    /**
     * Sets keyboard receiver.
     *
     * @param kb the keyboard
     */
    public void setKeyboard(biuoop.KeyboardSensor kb) {
        this.keyboard = kb;
    }

    /**
     * Sets speed of paddle.
     *
     * @param sp the speed
     */
    public void setSpeed(int sp) {
        this.speed = sp;
    }

    /**
     * Gets speed of paddle.
     *
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets left border of paddle.
     *
     * @param border the left border
     */
    public void setLeftBorder(int border) {
        this.leftBorder = border;
    }

    /**
     * Sets right border of paddle.
     *
     * @param border the right border
     */
    public void setRightBorder(int border) {
        this.rightBorder = border;
    }
}
