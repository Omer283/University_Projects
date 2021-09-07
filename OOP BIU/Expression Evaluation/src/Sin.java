/**
 * The type Sin.
 */
public class Sin extends UnaryExpression {

    /**
     * Instantiates a new Sin.
     *
     * @param e the expression
     */
    public Sin(Expression e) {
        super(e);
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Sin(getExp().assign(var, expression));
    }

    @Override
    protected double doAction(double x) {
        double rad = (Math.PI * x / 180);
        return Math.sin(rad);
    }

    @Override
    public Expression differentiate(String var) {
        Expression e = getExp();
        return new Mult(e.differentiate(var), new Cos(e));
    }

    @Override
    public String toString() {
        return "sin(" + getExp().toString() + ")";
    }

    @Override
    public Expression simplify() {
        Expression simp = getExp().simplify();
        if (simp.getVariables().isEmpty()) {
            double theta = 0;
            try {
                theta = simp.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Num(Math.sin(theta));
        } else {
            return new Sin(simp);
        }
    }
}
