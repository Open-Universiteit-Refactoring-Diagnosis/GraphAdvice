package nl.ou.refactoring.advice.nlp.grammar;

/**
 * The Class that implements this interface has register agreement with the phrase it is in.
 */
public interface HasRegisterAgreement extends SyntaxElement {
	/**
	 * Gets the register that is agreed to.
	 * @return The register that is agreed to.
	 */
	public GrammaticalRegister getRegisterAgreement();
	
	/**
	 * Sets the register that is agreed to.
	 * @param register The register that is agreed to.
	 */
	public void setRegisterAgreement(GrammaticalRegister register);
}
