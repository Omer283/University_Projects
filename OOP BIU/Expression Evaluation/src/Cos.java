/**
 * The type Cos.
 */
public class Cos extends UnaryExpression {

    /**
     * Instantiates a new Cos.
     *
     * @param e the inner expression
     */
    public Cos(Expression e) {
        super(e);
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Cos(getExp().assign(var, expression));
    }

    @Override
    public Expression simplify() {
        Expression simp = getExp().simplify();
        if (simp.getVariables().isEmpty()) {
            double theta = 0;
            try {
                theta = simp.evaluate(); //evaluate inner expression
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Num(Math.cos(theta));
        } else {
            return new Cos(getExp().simplify());
        }
    }

    @Override
    public Expression differentiate(String var) {
        Expression e = getExp();
        return new Neg(new Mult(e.differentiate(var), new Sin(e)));
    }

    @Override
    protected double doAction(double x) {
        double rad = (Math.PI * x / 180);
        return Math.cos(rad);
    }

    @Override
    public String toString() {
        return "cos(" + getExp().toString() + ")";
    }

}
