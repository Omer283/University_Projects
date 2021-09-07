/**
 * The interface Collidable.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public interface Collidable {
    /**
     * Gets collision rectangle of collidable object.
     * @return the collision rectangle
     */
    Rectangle getCollisionRectangle();

    /**
     * Notifies collidable when hit, and returns the ball's new velocity.
     * @param collisionPoint  the collision point
     * @param currentVelocity the current velocity
     * @param hitter the ball that hit the block
     * @return the new velocity
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
