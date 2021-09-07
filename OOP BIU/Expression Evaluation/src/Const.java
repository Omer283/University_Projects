import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Const.
 */
public class Const implements Expression {

    private String name;
    private double val;

    /**
     * Instantiates a new Const.
     *
     * @param nm the name
     * @param v  the value
     */
    public Const(String nm, double v) {
        name = nm;
        val = v;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return val;
    }

    @Override
    public double evaluate() throws Exception {
        return val;
    }

    @Override
    public List<String> getVariables() {
        return new ArrayList<>();
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return this;
    }

    @Override
    public Expression differentiate(String var) {
        return new Num(0);
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets name of constant.
     *
     * @return the name
     */
    protected String getName() {
        return name;
    }

    /**
     * Gets value of constant.
     *
     * @return the val
     */
    protected double getVal() {
        return val;
    }
}
