
import java.util.List;
import java.util.Map;

/**
 * The type Binary expression.
 */
public abstract class BinaryExpression extends BaseExpression {
    private Expression exp1, exp2;

    /**
     * Instantiates a new Binary expression.
     *
     * @param ee1 the first expression
     * @param ee2 the second expression
     */
    public BinaryExpression(Expression ee1, Expression ee2) {
        exp1 = ee1; exp2 = ee2;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        double d1 = exp1.evaluate(assignment), d2 = exp2.evaluate(assignment);
        return doAction(d1, d2);
    }

    @Override
    public double evaluate() throws Exception {
        double d1 = exp1.evaluate(), d2 = exp2.evaluate();
        return doAction(d1, d2);
    }

    @Override
    public List<String> getVariables() {
        List<String> l = exp1.getVariables(), tmpList = exp2.getVariables();
        for (String s : tmpList) {
            if (!l.contains(s)) { //no duplicates
                l.add(s);
            }
        }
        return l;
    }

    /**
     * Gets first Expression.
     *
     * @return the first Expression
     */
    protected Expression getExp1() {
        return exp1;
    }

    /**
     * Gets second Expression.
     *
     * @return the second Expression
     */
    protected Expression getExp2() {
        return exp2;
    }

    /**
     * Perform the operator of the class.
     *
     * @param x the first number
     * @param y the second number
     * @return the result
     */
    protected abstract double doAction(double x, double y);

}
