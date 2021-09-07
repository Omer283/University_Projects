import biuoop.KeyboardSensor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Selection collection.
 *
 * @param <T> the type parameter
 */
public class SelectionCollection<T> {
    private KeyboardSensor keyboard;
    private HashMap<String, T> keyToReturnValue;
    private List<String> optionNames;

    /**
     * Instantiates a new Selection collection.
     *
     * @param k the keyboard
     */
    public SelectionCollection(KeyboardSensor k) {
        keyboard = k;
        keyToReturnValue = new HashMap<>();
        optionNames = new LinkedList<>();
    }

    /**
     * Add selection.
     *
     * @param key         the key
     * @param optionName  the option name
     * @param returnValue the return value
     * @return if succeeded
     */
    protected boolean addSelection(String key, String optionName, T returnValue) {
        if (keyToReturnValue.containsKey(key)) {
            return false;
        } else {
            optionNames.add(optionName);
            keyToReturnValue.put(key, returnValue);
            return true;
        }
    }

    /**
     * Checks the value of the pressed object.
     *
     * @return the value
     */
    protected T checkPressed() {
        for (String key : keyToReturnValue.keySet()) {
            if (keyboard.isPressed(key)) {
                return keyToReturnValue.get(key);
            }
        }
        return null;
    }

    /**
     * Gets option names.
     *
     * @return the option names
     */
    protected List<String> getOptionNames() {
        return optionNames;
    }
}
