package nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.validation;

import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.validation.GraphValidationResultFixFailedException;

/**
 * An exception that is thrown if a fix for a code node's danger validation failed.
 */
public final class GraphNodeCodeDangerFixNotSupportedException extends GraphValidationResultFixFailedException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -6258420595692314880L;

	/**
	 * The code node that is not supported.
	 */
	private final GraphNodeCode codeNode;
	
	/**
	 * Initialises a new instance of {@link GraphNodeCodeDangerFixNotSupportedException}.
	 * @param codeNode The code node that is not supported.
	 */
	public GraphNodeCodeDangerFixNotSupportedException(GraphNodeCode codeNode) {
		this.codeNode = codeNode;
	}
	
	/**
	 * Gets the code node that is not supported.
	 * @return The code node that is not supported.
	 */
	public GraphNodeCode getCodeNode() {
		return this.codeNode;
	}
}