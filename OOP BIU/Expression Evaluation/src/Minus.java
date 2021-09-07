/**
 * The type Minus.
 */
public class Minus extends BinaryExpression {

    /**
     * Instantiates a new Minus.
     *
     * @param ee1 the subtractee
     * @param ee2 the subtracted
     */
    public Minus(Expression ee1, Expression ee2) {
        super(ee1, ee2);
    }


    @Override
    public Expression assign(String var, Expression expression) {
        return new Minus(getExp1().assign(var, expression), getExp2().assign(var, expression));
    }

    @Override
    protected double doAction(double x, double y) {
        return x - y;
    }

    @Override
    public String toString() {
        return "(" + getExp1().toString() + " - " + getExp2().toString() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        return new Minus(getExp1().differentiate(var), getExp2().differentiate(var));
    }

    @Override
    public Expression simplify() {
        Expression aSimple = getExp1().simplify(), bSimple = getExp2().simplify();
        if (aSimple.toString().equals(bSimple.toString())) { // x - x == 0
            return new Num(0);
        } else if (aSimple.toString().equals(new Num(0).toString())) { // 0 - x == -x
            return new Neg(bSimple);
        } else if (bSimple.toString().equals(new Num(0).toString())) { // x - 0 == x
            return aSimple;
        } else if (bSimple.getVariables().isEmpty() && aSimple.getVariables().isEmpty()) { //number
            double aa = 0;
            try {
                aa = aSimple.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            double bb = 0;
            try {
                bb = bSimple.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Num(aa - bb);
        } else {
            return new Minus(aSimple, bSimple);
        }
    }
}
