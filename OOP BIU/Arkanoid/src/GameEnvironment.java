/**
 * The class GameEnvironment.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class GameEnvironment {

    private java.util.List<Collidable> collidables;

    /**
     * Instantiates a new GameLogic.Game environment.
     */
    public GameEnvironment() {
        collidables = new java.util.ArrayList<Collidable>();
    }

    /**
     * Adds a collidable to the collidable list.
     * @param c the collidable
     */
    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    /**
     * Gets closest (from start point) collision of a collidable and trajectory.
     *
     * @param trajectory the trajectory
     * @return the info of the closest collision, or null if nonexistent
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        java.util.List<CollisionInfo> candidates = new java.util.ArrayList<>();
        CollisionInfo collisionInfo = null;
        double minDist = Double.MAX_VALUE - 5;
        for (Collidable c : collidables) {
            java.util.List<Point> pts = c.getCollisionRectangle().intersectionPoints(trajectory);
            if (pts == null) {
                continue;
            }
            for (Point pt : pts) {
                candidates.add(new CollisionInfo(pt, c));
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        java.util.List<CollisionInfo> finishCandidates = new java.util.ArrayList<>();
        for (CollisionInfo ci : candidates) {
            if (minDist - trajectory.start().distance(ci.collisionPoint()) > -Point.EPSILON) {
                if (minDist - trajectory.start().distance(ci.collisionPoint()) < Point.EPSILON) {
                    finishCandidates.add(ci);
                } else {
                    finishCandidates.clear();
                    finishCandidates.add(ci);
                    minDist = trajectory.start().distance(ci.collisionPoint());
                }
            }
        }
        if (finishCandidates.size() == 0) { //no collision
            return null;
        } else if (finishCandidates.size() == 1) { //only 1 good collision
            return finishCandidates.get(0);
        } else { //2 or more good collisions, get the closest one
            minDist = Double.MAX_VALUE - 5;
            CollisionInfo winner = null;
            for (CollisionInfo ci : finishCandidates) {
                if (trajectory.start().distance(ci.collisionObject().getCollisionRectangle().centerOfMass())
                        < minDist) {
                    minDist = trajectory.start().distance(ci.collisionObject().getCollisionRectangle().centerOfMass());
                    winner = ci;
                }
            }
            return winner;
        }
    }

    /**
     * Remove collidable from environment.
     *
     * @param c the collidable
     * @return whether succeeded
     */
    protected boolean removeCollidableFromEnvironment(Collidable c) {
        return collidables.remove(c);
    }
}
