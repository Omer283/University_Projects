/**
 * The type Div.
 */
public class Div extends BinaryExpression {

    /**
     * Instantiates a new Div.
     *
     * @param ee1 the numerator
     * @param ee2 the denominator
     */
    public Div(Expression ee1, Expression ee2) {
        super(ee1, ee2);
    }

    @Override
    protected double doAction(double x, double y) throws ArithmeticException {
        if (y == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return x / y;
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Div(getExp1().assign(var, expression), getExp2().assign(var, expression));
    }

    @Override
    public String toString() {
        return "(" + getExp1().toString() + " / " + getExp2().toString() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        boolean noVariableA = !getExp1().getVariables().contains(var),
                noVariableB = !getExp2().getVariables().contains(var);
        if (noVariableA && noVariableB) { //df/dx == 0
            return new Num(0);
        } else if (noVariableB) {
            return new Div(getExp1().differentiate(var), getExp2());
        } else { //f(x) / g(x)
            return new Div(new Minus(new Mult(getExp1().differentiate(var), getExp2()),
                    new Mult(getExp1(), getExp2().differentiate(var))), new Pow(getExp2(), new Num(2)));
        }
    }

    @Override
    public Expression simplify() {
        Expression aSimple = getExp1().simplify(), bSimple = getExp2().simplify();
        if (bSimple.toString().equals(new Num(1).toString())) {
            return aSimple;
        } else if (bSimple.toString().equals(aSimple.toString())) {
            return new Num(1);
        } else if (aSimple.getVariables().isEmpty() && bSimple.getVariables().isEmpty()) { //number
            double aa = 0, bb = 0;
            try {
                aa = aSimple.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bb = bSimple.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Num(aa / bb);
        } else {
            return new Div(aSimple, bSimple);
        }
    }
}
