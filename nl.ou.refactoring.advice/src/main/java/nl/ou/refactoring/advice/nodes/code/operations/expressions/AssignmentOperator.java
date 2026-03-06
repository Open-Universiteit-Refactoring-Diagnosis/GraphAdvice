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
	 * &lt;&lt;=
	 */
	SET_BITSHIFT_LEFT_SIGNED,
	
	/**
	 * &gt;&gt;=
	 */
	SET_BITSHIFT_RIGHT_SIGNED,
	
	/**
	 * &gt;&gt;&gt;=
	 */
	SET_BITSHIFT_RIGHT_UNSIGNED
}