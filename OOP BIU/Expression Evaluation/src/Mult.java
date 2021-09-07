/**
 * The type Mult.
 */
public class Mult extends BinaryExpression {

    /**
     * Instantiates a new Mult.
     *
     * @param ee1 the first expression
     * @param ee2 the second expression
     */
    public Mult(Expression ee1, Expression ee2) {
        super(ee1, ee2);
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Mult(getExp1().assign(var, expression), getExp2().assign(var, expression));
    }

    @Override
    protected double doAction(double x, double y) {
        return x * y;
    }

    @Override
    public String toString() {
        return "(" + getExp1().toString() + " * " + getExp2().toString() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        return new Plus(new Mult(getExp1().differentiate(var), getExp2()),
                new Mult(getExp1(), getExp2().differentiate(var)));
    }

    @Override
    public Expression simplify() {
        Expression aSimple = getExp1().simplify(), bSimple = getExp2().simplify();
        if (aSimple.toString().equals(new Num(1).toString())) { // x * 1 == x
            return bSimple;
        } else if (aSimple.toString().equals(new Num(0).toString())) { // x * 0 == 0
            return new Num(0);
        } else if (bSimple.toString().equals(new Num(1).toString())) { // 1 * x == x
            return aSimple;
        } else if (bSimple.toString().equals(new Num(0).toString())) { // 0 * x == 0
            return new Num(0);
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
            return new Num(aa * bb);
        } else {
            return new Mult(aSimple, bSimple);
        }
    }
}
