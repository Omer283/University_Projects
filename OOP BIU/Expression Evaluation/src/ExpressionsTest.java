import java.util.HashMap;
import java.util.Map;

/**
 * The type Expressions test.
 */
public class ExpressionsTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        Expression e = new Plus(new Mult(new Num(2), new Var("x")),
                new Plus(new Sin(new Mult(new Num(4), new Var("y"))),
                new Pow(new Var("e"), new Var("x"))));
        System.out.println(e);
        Map<String, Double> mp = new HashMap<>();
        mp.put("x", 2.0);
        mp.put("y", 0.25);
        mp.put("e", 2.71);
        System.out.println(e.evaluate(mp));
        System.out.println(e.differentiate("x"));
        System.out.println(e.differentiate("x").evaluate(mp));
        System.out.println(e.differentiate("x").simplify());
    }
}
