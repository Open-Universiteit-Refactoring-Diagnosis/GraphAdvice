package nl.ou.refactoring.advice.nlp.grammar.nouns;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.grammar.HasRegisterAgreement;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents a Pronoun in Natural Language.
 */
public final class Pronoun
		extends Noun
		implements HasRegisterAgreement {
	/**
	 * The token for a Pronoun.
	 */
	public static long TOKEN = Tokens.Nouns.PRONOUN;
	private final GrammaticalPerson person;
	private final GrammaticalNumber number;
	private GrammaticalRegister register;
	
	/**
	 * Initialises a new instance of {@link Pronoun}.
	 */
	public Pronoun(GrammaticalPerson person, GrammaticalGender gender, GrammaticalNumber number) {
		super(TOKEN, LexicalCategory.PROPER, SemanticClassification.CONCRETE, Countability.UNCOUNTABLE, () -> gender);
		this.person = person;
		this.number = number;
	}
	
	/**
	 * Gets the grammatical person of the pronoun.
	 * @return The grammatical person of the pronoun.
	 */
	public GrammaticalPerson getPerson() {
		return this.person;
	}
	
	/**
	 * Gets the grammatical number of the pronoun.
	 * @return The grammatical number of the pronoun.
	 */
	public GrammaticalNumber getNumber() {
		return this.number;
	}
	
	@Override
	public GrammaticalRegister getRegisterAgreement() {
		return this.register;
	}
	
	@Override
	public void setRegisterAgreement(GrammaticalRegister register) {
		this.register = register;
	}
	
	@Override
	protected Object clone() {
		return new Pronoun(this.person, this.getGender(), this.number);
	}
}