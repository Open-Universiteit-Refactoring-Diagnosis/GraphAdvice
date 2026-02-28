package nl.ou.refactoring.advice.nodes.code.operations.expressions;

/**
 * An assignment operator.
 */
public enum AssignmentOperator {
	/**
	 * =
	 */
	SET,
	
	/**
	 * *=
	 */
	SET_MULTIPLICATION,
	
	/**
	 * /=
	 */
	SET_DIVISION,
	
	/**
	 * %=
	 */
	SET_MODULUS,
	
	/**
	 * +=
	 */
	SET_ADDITION,
	
	/**
	 * -=
	 */
	SET_SUBTRACTION,
	
	/**
	 * <<=
	 */
	SET_BITSHIFT_LEFT_SIGNED,
	
	/**
	 * >>=
	 */
	SET_BITSHIFT_RIGHT_SIGNED,
	
	/**
	 * >>>=
	 */
	SET_BITSHIFT_RIGHT_UNSIGNED
}