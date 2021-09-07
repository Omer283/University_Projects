/**
 * The interface Sprite.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public interface Sprite {
    /**
     * The function that is called in order to draw the sprite on the drawsurface.
     * @param d the drawsurface
     */
    void drawOn(biuoop.DrawSurface d);

    /**
     * Function that is called when a frame passes.
     */
    void timePassed();
}
