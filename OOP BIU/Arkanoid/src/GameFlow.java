import biuoop.GUI;
import biuoop.KeyboardSensor;

import java.util.List;

/**
 * The type Game flow.
 */
public class GameFlow {
    private GUI gui;
    private AnimationRunner runner;
    private KeyboardSensor keyboard;
    private Counter score;

    /**
     * Instantiates a new Game flow.
     *
     * @param g the gui
     */
    public GameFlow(GUI g) {
        gui = g;
        runner = new AnimationRunner(g);
        keyboard = g.getKeyboardSensor();
        score = new Counter(0);
    }

    /**
     * Instantiates a new Game flow.
     *
     * @param keyboardSensor the keyboard sensor
     * @param ar             the ar
     * @param g              the g
     */
    public GameFlow(KeyboardSensor keyboardSensor, AnimationRunner ar, GUI g) {
        this(g);
        runner = ar;
        keyboard = keyboardSensor;
    }

    /**
     * Run levels.
     *
     * @param levels the levels
     */
    public void runLevels(List<LevelInformation> levels) {
        boolean winStatus = true; //if we win or lose
        for (LevelInformation levelInfo : levels) {
            GameLevel level = new GameLevel(levelInfo, score, gui, runner);
            level.initialize();
            level.run();
            if (level.ballCount() == 0) {
                winStatus = false;
                break;
            }
        }
        runner.run(new KeyPressStoppableAnimation(gui.getKeyboardSensor(), KeyboardSensor.SPACE_KEY,
                new EndScreenAnimation(winStatus, score)));
        HighScoreManager hsm = new HighScoreManager();
        hsm.update(score.getValue());
        score.reset();
    }
}