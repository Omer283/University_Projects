/**
 * The type Var.
 */
public class Var implements Expression {
    private String name;

    /**
     * Instantiates a new Var.
     *
     * @param varName the var name
     */
    public Var(String varName) {
        name = varName;
    }

    @Override
    public double evaluate(java.util.Map<String, Double> assignment) throws Exception {
        if (assignment != null && assignment.containsKey(name)) {
            return assignment.get(name);
        } else if (assignment != null) {
            throw new VariableNotAssignedException("Didn't assign all variables");
        } else {
            throw new NullPointerException("No assignment");
        }
    }

    @Override
    public double evaluate() throws Exception {
        throw new VariableNotAssignedException("Can't evaluate expression with variables with no initialization");
    }

    @Override
    public java.util.List<String> getVariables() {
        java.util.List<String> l = new java.util.LinkedList<>();
        l.add(name);
        return l;
    }

    @Override
    public Expression assign(String var, Expression expression) {
        if (var == name) {
            return expression;
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression differentiate(String var) {
        if (var == name) {
            return new Num(1);
        } else {
            return new Num(0);
        }
    }

    @Override
    public Expression simplify() {
        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    protected String getName() {
        return name;
    }
}
