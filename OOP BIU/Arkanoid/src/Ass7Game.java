import biuoop.GUI;
import biuoop.KeyboardSensor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.util.List;
import java.io.FileNotFoundException;

/**
 * The type Ass 7 game.
 */
public class Ass7Game {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        //args
        String filePath;
        if (args != null && args.length >= 1) {
            filePath = args[0]; //decides path
        } else {
            filePath = "resources\\level_definition.txt";
        }
        HighScoreManager highScoreManager = new HighScoreManager();
        LevelSpecificationReader a = new LevelSpecificationReader();
        Reader r = new BufferedReader(new FileReader(
                filePath));
        List<LevelInformation> l = a.fromReader(r);
        GUI g = new GUI("Arkanoid - Finale", 800, 600);
        AnimationRunner runner = new AnimationRunner(g);
        MenuAnimation<Task<Void>> menuAnimation = new MenuAnimation<Task<Void>>(
                "Arkanoid", g.getKeyboardSensor());
        Animation animation = new KeyPressStoppableAnimation(g.getKeyboardSensor(),
                KeyboardSensor.SPACE_KEY, new HighScoresAnimation(highScoreManager));
        menuAnimation.addSelection("h", "High Score", new Task<Void>() { //anonymous
            @Override
            public Void run() {
                runner.run(animation);
                return null;
            }
        });
        menuAnimation.addSelection("s", "Start Game", new Task<Void>() {
            private GameFlow gameFlow = new GameFlow(g);
            @Override
            public Void run() {
                LevelSpecificationReader a = new LevelSpecificationReader();
                Reader r = null;
                try {
                    r = new BufferedReader(new FileReader(
                            filePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    List<LevelInformation> l = a.fromReader(r);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gameFlow.runLevels(l);
                runner.run(animation);
                return null;
            }
        });
        menuAnimation.addSelection("q", "Quit", new Task<Void>() {
            @Override
            public Void run() {
                System.exit(0);
                return null;
            }
        });
        ApplicationFlow p = new ApplicationFlow(g, menuAnimation);
        p.run();
    }
}
