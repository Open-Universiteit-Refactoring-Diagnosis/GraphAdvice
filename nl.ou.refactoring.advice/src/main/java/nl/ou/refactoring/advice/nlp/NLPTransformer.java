package nl.ou.refactoring.advice.nlp;

/**
 * Transforms Natural Language Processing artifacts.
 */
public final class NLPTransformer {
	private NLPTransformer() { }

	/**
	 * Capitalises the first letter of the specified {@link String}.
	 * @param original The original {@link String}.
	 * @return A copy of the original {@link String} but with the first letter capitalised. If the original was null or empty, the identity is returned.
	 */
	public static String capitaliseFirstLetter(String original) {
		if (original == null || original.length() == 0) {
			return original;
		}
		if (original.length() == 1) {
			return original.toUpperCase();
		}
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
}
