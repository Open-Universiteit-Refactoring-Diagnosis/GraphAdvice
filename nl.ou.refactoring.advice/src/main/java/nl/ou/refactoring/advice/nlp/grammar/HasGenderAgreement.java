package nl.ou.refactoring.advice.nlp.grammar;

/**
 * The Class that implements this interface has Gender agreement with the phrase it is in.
 */
public interface HasGenderAgreement extends SyntaxElement {
	/**
	 * Gets the Gender that is agreed to.
	 * @return The Gender that is agreed to.
	 */
	public GrammaticalGender getGenderAgreement();
	
	/**
	 * Sets the Gender that is agreed to.
	 * @param gender The Gender that is agreed to.
	 */
	public void setGenderAgreement(GrammaticalGender gender);
}
