import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Block.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class Block implements Collidable, Sprite, GameObject, HitNotifier {

    private List<HitListener> hitListeners;
    private Rectangle blockRectangle;
    private BlockBackground background;
    private boolean killerBlock = false, borderBlock = false;

    /**
     * Instantiates a new Block, given the rectangle.
     *
     * @param rect the rectangle
     */
    public Block(Rectangle rect) {
        blockRectangle = rect;
        hitListeners = new LinkedList<>();
        background = new ColorBlockBackground(java.awt.Color.BLACK, java.awt.Color.BLACK, rect);
    }

    /**
     * Instantiates a new Block.
     *
     * @param rect            the rect
     * @param blockBackground the block background
     */
    public Block(Rectangle rect, BlockBackground blockBackground) {
        blockRectangle = rect;
        hitListeners = new LinkedList<>();
        background = blockBackground;
    }

    /**
     * Instantiates a new Block.
     *
     * @param rect      the rect
     * @param fillColor the fill color
     */
    public Block(Rectangle rect, Color fillColor) {
        blockRectangle = rect;
        hitListeners = new LinkedList<>();
        background = new ColorBlockBackground(fillColor, Color.BLACK, blockRectangle);
    }

    /**
     * returns the collision rectangle.
     * @return the rectangle
     */
    public Rectangle getCollisionRectangle() {
        return blockRectangle;
    }

    /**
     * Notifies the block that it was hit.
     * @param collisionPoint the point of collision
     * @param currentVelocity the velocity of the ball
     * @param hitter the ball that hit the block
     * @return the updated velocity
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.notifyHit(hitter);
        double[] x = blockRectangle.getxVals(), y = blockRectangle.getyVals();
        if (collisionPoint.equals(new Point(x[0], y[0]))) { //detects if hit corners
            return new Velocity(-Math.abs(currentVelocity.getDx()), -Math.abs(currentVelocity.getDy()));
        } else if (collisionPoint.equals(new Point(x[1], y[0]))) {
            return new Velocity(Math.abs(currentVelocity.getDx()), -Math.abs(currentVelocity.getDy()));
        } else if (collisionPoint.equals(new Point(x[0], y[1]))) {
            return new Velocity(-Math.abs(currentVelocity.getDx()), Math.abs(currentVelocity.getDy()));
        } else if (collisionPoint.equals(new Point(x[1], y[1]))) {
            return new Velocity(Math.abs(currentVelocity.getDx()), Math.abs(currentVelocity.getDy()));
        } else if (Math.abs(collisionPoint.getX() - blockRectangle.getUpperLeft().getX()) <= Point.EPSILON
                || Math.abs(collisionPoint.getX() - blockRectangle.getBottomRight().getX()) <= Point.EPSILON) {
            //hit horizontally
            return new Velocity(-currentVelocity.getDx(), currentVelocity.getDy());
        } else if (Math.abs(collisionPoint.getY() - blockRectangle.getUpperLeft().getY()) <= Point.EPSILON
         || Math.abs(collisionPoint.getY() - blockRectangle.getBottomRight().getY()) <= Point.EPSILON) {
            //hit vertically
            return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
        } else {
            return currentVelocity;
        }
    }

    /**
     * Draws the block on the GUI.
     * @param d the DrawSurface
     */
    public void drawOn(biuoop.DrawSurface d) {
        background.draw(d);
    }

    /**
     * Updates when a frame has passed (currently useless).
     */
    public void timePassed() {

    }

    /**
     * Add the block to game collections.
     * @param g the game
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    @Override
    public void removeFromGame(GameLevel g) {
        g.removeCollidable(this);
        g.removeSprite(this);
    }

    @Override
    public void addHitListener(HitListener hl) {
        if (hl != null) {
            hitListeners.add(hl);
        }
    }

    @Override
    public void removeHitListener(HitListener hl) {
        if (hl != null) {
            hitListeners.remove(hl);
        }
    }

    /**
     * Notifies the listeners that the hit occured.
     * @param hitter the ball that hit
     */
    private void notifyHit(Ball hitter) {
        List<HitListener> listenersCopy = new LinkedList<>(hitListeners);
        for (HitListener hl : listenersCopy) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * Sets killer.
     */
    public void setKiller() {
        this.killerBlock = true;
    }

    /**
     * Sets border.
     */
    public void setBorder() {
        this.borderBlock = true;
    }

    /**
     * Is border.
     *
     * @return true iff border
     */
    public boolean isBorder() {
        return this.borderBlock;
    }

    /**
     * Is killer boolean.
     *
     * @return true iff killer
     */
    public boolean isKiller() {
        return this.killerBlock;
    }
}
