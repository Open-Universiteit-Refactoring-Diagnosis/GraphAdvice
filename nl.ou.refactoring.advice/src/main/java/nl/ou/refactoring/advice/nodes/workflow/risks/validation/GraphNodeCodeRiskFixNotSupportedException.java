package nl.ou.refactoring.advice.nodes.workflow.risks.validation;

import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;
import nl.ou.refactoring.advice.validation.GraphValidationResultFixFailedException;

public class GraphNodeCodeRiskFixNotSupportedException extends GraphValidationResultFixFailedException {

	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -6258420595692314880L;

	/**
	 * The code node that is not supported.
	 */
	private final GraphNodeCode codeNode;
	
	/**
	 * Initialises a new instance of {@link GraphNodeCodeRiskFixNotSupportedException}.
	 * @param codeNode The code node that is not supported.
	 */
	public <TNodeRisk extends GraphNodeRisk> GraphNodeCodeRiskFixNotSupportedException(GraphNodeCode codeNode) {
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
