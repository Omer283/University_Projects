
import java.util.List;
import java.util.Map;

/**
 * The type Unary expression.
 */
public abstract class UnaryExpression extends BaseExpression {
    private Expression exp1;

    /**
     * Instantiates a new Unary expression.
     *
     * @param ee the expression
     */
    public UnaryExpression(Expression ee) {
        exp1 = ee;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        double d = exp1.evaluate(assignment);
        return doAction(d);
    }

    @Override
    public double evaluate() throws Exception {
        double d = exp1.evaluate();
        return doAction(d);
    }

    @Override
    public List<String> getVariables() {
        return exp1.getVariables();
    }

    /**
     * Gets inner expression.
     *
     * @return the expression
     */
    protected Expression getExp() {
        return exp1;
    }

    /**
     * Perform the operator on a number.
     *
     * @param x the number
     * @return the result
     */
    protected abstract double doAction(double x);

}
