import java.util.ArrayList;
import java.util.List;

/**
 * The type Num.
 */
public class Num implements Expression {
    private double val;

    /**
     * Instantiates a new Num.
     *
     * @param value the value
     */
    public Num(double value) {
        val = value;
    }

    @Override
    public double evaluate(java.util.Map<String, Double> assignment) throws Exception {
        return val;
    }

    @Override
    public double evaluate() throws Exception {
        return val;
    }

    @Override
    public java.util.List<String> getVariables() {
        return new ArrayList<>();
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return this;
    }

    @Override
    public String toString() {
        return Double.toString(val);
    }

    @Override
    public Expression differentiate(String var) {
        return new Num(0);
    }

    @Override
    public Expression simplify() {
        return this;
    }

    /**
     * Gets the number's value.
     *
     * @return the val
     */
    protected double getVal() {
        return val;
    }
}
