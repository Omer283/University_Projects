/**
 * The type Log.
 */
public class Log extends BinaryExpression {

    /**
     * Instantiates a new Log.
     *
     * @param ee1 the base
     * @param ee2 the second expression
     */
    public Log(Expression ee1, Expression ee2) {
        super(ee1, ee2);
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Log(getExp1().assign(var, expression), getExp2().assign(var, expression));
    }

    @Override
    protected double doAction(double x, double y) throws ArithmeticException {
        if (y == 1 || y <= 0 || x <= 0) {
            throw new ArithmeticException("Invalid logarithm parameters"); //invalid in real numbers
        } else {
            return Math.log(y) / Math.log(x);
        }
    }

    @Override
    public String toString() {
        return "log(" + getExp1().toString() + ", " + getExp2().toString() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        Expression e1 = getExp1(), e2 = getExp2();
        boolean firstHasVariable = e1.getVariables().contains(var),
                secondHasVariable = e2.getVariables().contains(var);
        if (!firstHasVariable && !secondHasVariable) { //constant
            return new Num(0);
        } else if (!firstHasVariable) { //base is constant
            return new Mult(e2.differentiate(var), new Div(new Ln(e1), e2));
        } else { //general formula
            return new Div(new Minus(new Mult(new Div(e2.differentiate(var), e2), new Ln(e1)),
                    new Mult(new Div(e1.differentiate(var), e1), new Ln(e2))), new Pow(e1, new Num(2)));
        }
    }

    @Override
    public Expression simplify() {
        Expression aSimple = getExp1().simplify(), bSimple = getExp2().simplify();
        boolean noVariablesA = aSimple.getVariables().isEmpty(), noVariablesB = bSimple.getVariables().isEmpty();
        if (noVariablesA && noVariablesB) { //is a number
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
            double res = 0;
            try {
                res = doAction(aa, bb);
            } catch (ArithmeticException e) {
                e.printStackTrace();
            }
            return new Num(res);
        } else if (bSimple.toString().equals(aSimple.toString())) { //log(x, x) == 1
            return new Num(1);
        } else {
            return new Log(aSimple, bSimple);
        }
    }
}
