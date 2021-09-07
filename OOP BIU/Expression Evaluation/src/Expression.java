/**
 * The interface Expression.
 */
public interface Expression {

    /**
     * Evaluate the expression, using substitutions in assignment.
     *
     * @param assignment the assignment
     * @return the value
     * @throws Exception if there's a variable in the expression which isn't in the assignment
     */
    double evaluate(java.util.Map<String, Double> assignment) throws Exception;

    /**
     * A convenience method. Like the `evaluate(assignment)` method above, but uses an empty assignment.
     *
     * @return the value
     * @throws Exception if there's a variable in the expression
     */
    double evaluate() throws Exception;

    /**
     * Returns a list of the variables in the expression.
     *
     * @return the variable list
     */
    java.util.List<String> getVariables();
    /**
     * Converts expression to string.
     * @return the string
     */
    String toString();

    /**
     * Returns a new expression in which all occurrences
     * of the variable var are replaced with the provided expression (Does not modify the
     * current expression).
     *
     * @param var        the var
     * @param expression the expression
     * @return the new expression
     */
    Expression assign(String var, Expression expression);

    /**
     * Return the differentiated expression (by var).
     *
     * @param var the var
     * @return the expression
     */
    Expression differentiate(String var);

    /**
     * Simplify expression.
     *
     * @return the simplified Expression
     */
    Expression simplify();

}