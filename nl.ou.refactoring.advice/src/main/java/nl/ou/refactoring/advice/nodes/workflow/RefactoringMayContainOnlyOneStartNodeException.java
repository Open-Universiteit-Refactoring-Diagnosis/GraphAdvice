package nl.ou.refactoring.advice.nodes.workflow;

/**
 * An exception that is thrown if a graph contains more than one start node.
 */
public final class RefactoringMayContainOnlyOneStartNodeException extends Exception {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 1469593672534480283L;
	
	/**
	 * Initialises a new instance of {@link RefactoringMayContainOnlyOneStartNodeException}.
	 */
	public RefactoringMayContainOnlyOneStartNodeException() {
	}

	@Override
	public String getLocalizedMessage() {
		return "A refactoring cannot contain more than one start node.";
	}
}
