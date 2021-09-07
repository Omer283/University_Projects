import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

/**
 * The type Animation runner.
 */
public class AnimationRunner {
    private GUI gui;
    private int framesPerSecond = 60;
    private Sleeper sleeper;

    /**
     * Instantiates a new Animation runner.
     * @param g the gui
     */
    public AnimationRunner(GUI g) {
        this.gui = g;
        this.sleeper = new Sleeper();
    }

    /**
     * Instantiates a new Animation runner.
     *
     * @param g   the gui
     * @param fps the fps
     */
    public AnimationRunner(GUI g, int fps) {
        this(g);
        this.framesPerSecond = fps;
    }

    /**
     * Runs the animation.
     * @param animation the animation
     */
    public void run(Animation animation) {
        int millisecondsPerFrame = 1000 / framesPerSecond;
        while (!animation.shouldStop()) {
            long startTime = System.currentTimeMillis(); // timing
            DrawSurface d = gui.getDrawSurface();
            animation.doOneFrame(d);
            gui.show(d);
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                this.sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
        DrawSurface d = gui.getDrawSurface(); //finish it off
        animation.doOneFrame(d);
        gui.show(d);
        animation.forceContinue();
    }
}