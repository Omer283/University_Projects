/**
 * The type Pow.
 */
public class Pow extends BinaryExpression {

    /**
     * Instantiates a new Pow.
     *
     * @param ee1 the base
     * @param ee2 the exponent
     */
    public Pow(Expression ee1, Expression ee2) {
        super(ee1, ee2);
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Pow(getExp1().assign(var, expression), getExp2().assign(var, expression));
    }

    @Override
    protected double doAction(double x, double y) {
        return Math.pow(x, y);
    }

    @Override
    public String toString() {
        return "(" + getExp1().toString() + "^" + getExp2().toString() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        Expression e1 = getExp1(), e2 = getExp2();
        boolean firstHasVariable = e1.getVariables().contains(var),
                secondHasVariable = e2.getVariables().contains(var);
        if (!firstHasVariable && !secondHasVariable) { //constant
            return new Num(0);
        } else if (firstHasVariable && !secondHasVariable) { //x^n -> nx^n-1
            return new Mult(e2, new Pow(e1, new Minus(e2, new Num(1))));
        } else if (!firstHasVariable && secondHasVariable) {
            if (e1.toString().equals("e")) { //ln
                return new Mult(e2.differentiate(var), this);
            } else {
                return new Mult(new Mult(e2.differentiate(var), this), new Ln(e1));
            }
        } else { //general
            return new Plus(new Mult(new Mult(new Pow(e1, e2), e2.differentiate(var)), new Ln(e1)),
                    new Mult(e1.differentiate(var), new Mult(e2, new Pow(e1, new Minus(e2, new Num(1))))));
        }
    }

    @Override
    public Expression simplify() {
        Expression aSimple = getExp1().simplify(), bSimple = getExp2().simplify();
        if (aSimple.getVariables().isEmpty() && bSimple.getVariables().isEmpty()) {
            double aa = 0, bb = 0;
            try {
                aa = aSimple.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bb = aSimple.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Num(Math.pow(aa, bb));
        } else {
            return new Pow(aSimple, bSimple);
        }
    }
}
