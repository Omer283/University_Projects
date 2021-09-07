
import biuoop.DrawSurface;

import java.util.LinkedList;

/**
 * The type Sprite collection.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class SpriteCollection {
    private java.util.List<Sprite> spriteLinkedList;

    /**
     * Instantiates a new SpriteCollection.
     */
    public SpriteCollection() {
        spriteLinkedList = new LinkedList<>();
    }

    /**
     * Add sprite to sprites list.
     *
     * @param s the sprite
     */
    public void addSprite(Sprite s) {
        spriteLinkedList.add(s);
    }

    /**
     * Notify all sprites that time has passed.
     */
    public void notifyAllTimePassed() {
        java.util.List<Sprite> spritesCopy = new LinkedList<>(spriteLinkedList);
        for (Sprite sp : spritesCopy) {
            sp.timePassed();
        }
    }

    /**
     * Draw all sprites on drawsurface.
     *
     * @param d the drawsurface
     */
    public void drawAllOn(DrawSurface d) {
        java.util.List<Sprite> spritesCopy = new LinkedList<>(spriteLinkedList);
        for (Sprite sp : spritesCopy) {
            sp.drawOn(d);
        }
    }

    /**
     * Remove sprite from collection boolean.
     *
     * @param s the sprite
     * @return if succeeded
     */
    protected boolean removeSpriteFromCollection(Sprite s) {
        return spriteLinkedList.remove(s);
    }
}
