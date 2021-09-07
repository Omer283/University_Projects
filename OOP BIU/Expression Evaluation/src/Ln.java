/**
 * The type Ln, used in derivatives.
 */
public class Ln extends Log {

    /**
     * Instantiates a new Ln.
     *
     * @param ee1 the expression
     */
    public Ln(Expression ee1) {
        super(new Const("e", Math.E), ee1);
    }

    @Override
    public Expression differentiate(String var) {
        return new Div(getExp2().differentiate(var), getExp2());
    }
}
