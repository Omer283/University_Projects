/**
 * The class CollisionInfo.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class CollisionInfo {
    private Point collisionPt;
    private Collidable object;

    /**
     * Instantiates a new Collision info.
     * @param pt         the pt
     * @param collidable the collidable
     */
    public CollisionInfo(Point pt, Collidable collidable) {
        collisionPt = pt;
        object = collidable;
    }

    /**
     * Collision point point.
     *
     * @return the point
     */
    public Point collisionPoint() {
        return collisionPt;
    }

    /**
     * Collision object collidable.
     *
     * @return the collidable
     */
    public Collidable collisionObject() {
        return object;
    }

    /**
     * Sets collision pt.
     *
     * @param pt the pt
     */
    public void setCollisionPt(Point pt) {
        this.collisionPt = pt;
    }

    /**
     * Sets object.
     *
     * @param obj the obj
     */
    public void setObject(Collidable obj) {
        this.object = obj;
    }
}
