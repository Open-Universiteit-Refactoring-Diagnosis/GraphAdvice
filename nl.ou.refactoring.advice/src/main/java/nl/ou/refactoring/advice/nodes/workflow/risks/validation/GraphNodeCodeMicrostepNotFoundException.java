package nl.ou.refactoring.advice.nodes.workflow.risks.validation;

import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.validation.GraphValidationResultFixFailedException;

/**
 * An exception that is thrown if the microstep that manipulates a code node could not be found.
 */
public final class GraphNodeCodeMicrostepNotFoundException extends GraphValidationResultFixFailedException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 4872910672995192851L;
	
	/**
	 * The code node involved with the microstep.
	 */
	private final GraphNodeCode codeNode;

	/**
	 * Initialises a new instance of {@link GraphNodeCodeMicrostepNotFoundException}.
	 * @param codeNode The code node involved with the microstep.
	 */
	public GraphNodeCodeMicrostepNotFoundException(GraphNodeCode codeNode) {
		this.codeNode = codeNode;
	}

	/**
	 * Gets the code node involved with the microstep.
	 * @return The code node involved with the microstep.
	 */
	public GraphNodeCode getCodeNode() {
		return this.codeNode;
	}
}