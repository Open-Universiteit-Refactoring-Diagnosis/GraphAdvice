package nl.ou.refactoring.advice.api;

/**
 * An identifier of a pre-defined Refactoring Advice Graph (RAG) template.
 */
public enum RefactoringTemplateIdentifier {
	/**
	 * The "Move Field" refactoring.
	 */
	MOVE_FIELD,
	
	/**
	 * The "Move Method" refactoring.
	 */
	MOVE_METHOD,
	
	/**
	 * The "Rename Field" refactoring.
	 */
	RENAME_FIELD,
	
	/**
	 * The "Rename Method" refactoring.
	 */
	RENAME_METHOD,
}
