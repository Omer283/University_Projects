/**
 * The type Neg.
 */
public class Neg extends UnaryExpression {

    /**
     * Instantiates a new Neg.
     *
     * @param e the expression
     */
    public Neg(Expression e) {
        super(e);
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Neg(getExp().assign(var, expression));
    }

    @Override
    public String toString() {
        return "(-" + getExp().toString() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        return new Neg(getExp().differentiate(var));
    }

    @Override
    public Expression simplify() {
        Expression simp = getExp().simplify();
        if (simp.getVariables().isEmpty()) { //number
            double a = 0;
            try {
                a = simp.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Num(-a);
        } else {
            return new Neg(simp);
        }
    }

    @Override
    protected double doAction(double x) {
        return -x;
    }
}
