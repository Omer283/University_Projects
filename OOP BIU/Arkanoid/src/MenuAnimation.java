import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

/**
 * The type Menu animation.
 *
 * @param <T> the type parameter
 */
public class MenuAnimation<T> implements Menu<T> {
    private String name;
    private KeyboardSensor keyboard;
    private SelectionCollection<T> collection;
    private boolean stop;

    /**
     * Instantiates a new Menu animation.
     *
     * @param menuName the menu name
     * @param k        the keyboard
     */
    public MenuAnimation(String menuName, KeyboardSensor k) {
        name = menuName;
        keyboard = k;
        collection = new SelectionCollection<>(k);
        stop = false;
    }

    @Override
    public void addSelection(String key, String message, T returnVal) {
        collection.addSelection(key, message, returnVal);
    }

    @Override
    public T getStatus() {
        return collection.checkPressed();
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        int height = d.getHeight(), width = d.getWidth();
        d.drawText((width - 100) / 2, height / 6, name, 32);
        int specifiedHeight = (5 * height / 8), optionAmt = collection.getOptionNames().size();
        int delta = width / optionAmt, start = 50;
        for (String opt : collection.getOptionNames()) {
            d.drawText(start, specifiedHeight, opt, 32);
            start += delta;
        }
        T status = getStatus();
        if (status != null) {
            stop = true;
        }
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }

    @Override
    public void forceContinue() {
        stop = false;
    }


}
