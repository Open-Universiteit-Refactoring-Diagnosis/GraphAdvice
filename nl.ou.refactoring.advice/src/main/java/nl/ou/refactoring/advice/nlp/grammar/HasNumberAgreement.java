package nl.ou.refactoring.advice.nlp.grammar;

/**
 * The Class that implements this interface has grammatical Number agreement with the phrase it is in.
 */
public interface HasNumberAgreement extends SyntaxElement {
	/**
	 * Gets the grammatical number that is agreed to.
	 * @return The grammatical number that is agreed to.
	 */
	public GrammaticalNumber getNumberAgreement();
	
	/**
	 * Sets the grammatical number that is agreed to.
	 * @param number The grammatical number that is agreed to.
	 */
	public void setNumberAgreement(GrammaticalNumber number);
}