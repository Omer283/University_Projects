import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

/**
 * The type Key press stoppable animation.
 */
public class KeyPressStoppableAnimation implements Animation {
    private String keyPressed;
    private Animation mainAnimation;
    private KeyboardSensor keyboardSensor;
    private boolean stop = false, isAlreadyPressed = true; //VERIFY THAT WORKS

    /**
     * Instantiates a new Key press stoppable animation.
     *
     * @param sensor    the sensor
     * @param key       the key
     * @param animation the animation
     */
    public KeyPressStoppableAnimation(KeyboardSensor sensor, String key, Animation animation) {
        keyboardSensor = sensor;
        keyPressed = key;
        mainAnimation = animation;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        mainAnimation.doOneFrame(d);
        if (keyboardSensor.isPressed(keyPressed) && !isAlreadyPressed) {
            stop = true;
        } else if (!keyboardSensor.isPressed(keyPressed)) {
            isAlreadyPressed = false;
        }
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }

    @Override
    public void forceContinue() {
        stop = false;
        isAlreadyPressed = true;
    }
}
