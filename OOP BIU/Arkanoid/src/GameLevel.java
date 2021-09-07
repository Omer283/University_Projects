import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;
import java.awt.Color;
import java.util.List;

/**
 * The type GameLogic.Game.
 * Name: Omer Ronen
 * ID: 212775803
 * Nickname: ronenom
 */
public class GameLevel implements Animation {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter blockCounter, ballCounter, scoreCounter;
    private BlockRemover blockRemover;
    private BallRemover ballRemover;
    private ScoreTrackingListener scoreTrackingListener;
    private ScoreIndicator scoreIndicator;
    private biuoop.GUI gui;
    private AnimationRunner runner;
    private KeyboardSensor keyboard;
    private LevelInformation levelInformation;
    private Sprite background;
    private boolean running = true;
    /**
     * The constant WIDTH.
     */
    public static final int WIDTH = 800,
    /**
     * The Height.
     */
    HEIGHT = 600;

    /**
     * Instantiates a new Game.
     *
     * @param info  the information for the level
     * @param score the current score
     */
    public GameLevel(LevelInformation info, Counter score) {
        levelInformation = info;
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        blockCounter = new Counter(info.numberOfBlocksToRemove());
        ballCounter = new Counter(info.numberOfBalls());
        scoreCounter = score;
        scoreIndicator = new ScoreIndicator(scoreCounter, info.levelName());
        scoreTrackingListener = new ScoreTrackingListener(scoreCounter);
        blockRemover = new BlockRemover(this, blockCounter);
        ballRemover = new BallRemover(this, ballCounter);
        background = info.getBackground();
    }

    /**
     * Instantiates a new Game level.
     *
     * @param info  the info
     * @param score the score
     * @param g     the gui
     * @param ar    the animation runner
     */
    public GameLevel(LevelInformation info, Counter score, GUI g, AnimationRunner ar) {
        this(info, score);
        gui = g;
        keyboard = g.getKeyboardSensor();
        runner = ar;
    }

    /**
     * Add collidable to gameEnvironment.
     *
     * @param c the collidable
     */
    public void addCollidable(Collidable c) {
        environment.addCollidable(c);
    }

    /**
     * Add sprite to sprite list.
     *
     * @param s the sprite
     */
    public void addSprite(Sprite s) {
        sprites.addSprite(s);
    }

    /**
     * Initialize.
     */
    public void initialize() {
        addPaddle();
        addBalls();
        addBlocks();
        sprites.addSprite(scoreIndicator);
    }

    /**
     * Adds blocks to game.
     */
    private void addBlocks() {
        for (Block block : levelInformation.blocks()) {
            if (!block.isBorder()) {
                block.addHitListener(scoreTrackingListener);
                block.addHitListener(blockRemover);
            } else if (block.isKiller()) {
                block.addHitListener(ballRemover);
            }
            block.addToGame(this);
        }
    }
    /**
     * Adds Balls to game.
     */
    private void addBalls() {
        List<Point> positions = levelInformation.initialBallPositions();
        List<Velocity> speeds = levelInformation.initialBallVelocities();
        for (int i = 0; i < levelInformation.numberOfBalls(); i++) {
            Ball b = new Ball(positions.get(i));
            b.setGameEnvironment(environment);
            b.setVelocity(speeds.get(i));
            b.addToGame(this);
        }
    }

    /**
     * Adds paddle to game.
     */
    private void addPaddle() {
        Paddle p = new Paddle(levelInformation.paddleWidth());
        p.setSpeed(levelInformation.paddleSpeed());
        p.setKeyboard(keyboard);
        p.setColor(Color.yellow);
        p.addToGame(this);
    }

    /**
     * Runs the game.
     */
    public void run() {
        CountdownAnimation countdownAnimation = new CountdownAnimation(2, 3, sprites);
        countdownAnimation.setBackground(background);
        runner.run(countdownAnimation);
        runner.run(this);
        Sleeper sleeper = new Sleeper();
        sleeper.sleepFor(1000);
    }

    /**
     * Remove collidable.
     *
     * @param c the c
     */
    public void removeCollidable(Collidable c) {
        environment.removeCollidableFromEnvironment(c);
    }

    /**
     * Remove sprite.
     *
     * @param s the s
     */
    public void removeSprite(Sprite s) {
        sprites.removeSpriteFromCollection(s);
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        if (background != null) {
            background.drawOn(d);
        }
        this.sprites.drawAllOn(d); //draws all sprites
        this.sprites.notifyAllTimePassed(); //notifies all objects on screen that a frame has passed
        if (ballCounter.getValue() == 0) {
            this.running = false;
        } else if (blockCounter.getValue() <= 0 && !shouldStop()) {
            scoreCounter.increase(100);
            this.running = false;
        } else if (keyboard.isPressed("p")) {
            runner.run(new KeyPressStoppableAnimation(keyboard, KeyboardSensor.SPACE_KEY, new PauseScreen()));
        }
    }

    @Override
    public boolean shouldStop() {
        return !this.running;
    }

    @Override
    public void forceContinue() {
        running = true;
    }

    /**
     * Ball count int.
     *
     * @return the int
     */
    public int ballCount() {
        return ballCounter.getValue();
    }

}
