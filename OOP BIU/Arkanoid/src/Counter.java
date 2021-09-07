

/**
 * The type Counter.
 */
public class Counter {
    private int count;

    /**
     * Instantiates a new Counter.
     *
     * @param initialValue the initial value
     */
    public Counter(int initialValue) {
        count = initialValue;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return count;
    }

    /**
     * Increase by number.
     *
     * @param number the number
     */
    protected void increase(int number) {
        count += number;
    }

    /**
     * Decrease by number.
     *
     * @param number the number
     * @throws CounterBelowZeroException if we deduct below zero, throws an exception
     */
    public void decrease(int number) {
        count -= number;
    }

    /**
     * Resets counter.
     */
    public void reset() {
        count = 0;
    }
}
