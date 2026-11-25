package nl.ou.refactoring.advice.nodes.code;

/**
 * Defines the scope of a code symbol in a Refactoring Advice Graph (RAG) code
 * context.
 */
public enum GraphNodeCodeAccessModifier {
	/**
	 * The code symbol is private (only accessible within its own scope).
	 */
	PRIVATE,

	/**
	 * The code symbol is visible in its own package but not in other packages.
	 */
	PACKAGE,

	/**
	 * The code symbol is protected, so only visible in its own package, not in
	 * other packages, but also visible in its inheritance tree.
	 */
	PROTECTED,

	/**
	 * The code symbol is public, therefore always visible.
	 */
	PUBLIC
}
