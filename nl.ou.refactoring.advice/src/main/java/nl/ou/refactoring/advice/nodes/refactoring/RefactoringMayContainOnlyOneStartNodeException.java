package nl.ou.refactoring.advice.nodes.refactoring;

public final class RefactoringMayContainOnlyOneStartNodeException extends Exception {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 1469593672534480283L;

	@Override
	public String getLocalizedMessage() {
		return "A refactoring cannot contain more than one start node.";
	}
}
