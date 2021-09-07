import biuoop.GUI;

/**
 * The type Application flow.
 */
public class ApplicationFlow {
    private GUI gui;
    private AnimationRunner runner;
    private MenuAnimation<Task<Void>> menuAnimation;

    /**
     * Instantiates a new ApplicationFlow.
     *
     * @param g    the gui
     * @param menu the menu
     */
    public ApplicationFlow(GUI g, MenuAnimation<Task<Void>> menu) {
        gui = g;
        runner = new AnimationRunner(g);
        menuAnimation = menu;
    }

    /**
     * Run.
     */
    public void run() {
        while (true) {
            runner.run(menuAnimation);
            Task<Void> task = menuAnimation.getStatus();
            if (task != null) {
                task.run();
            }
        }
    }
}
