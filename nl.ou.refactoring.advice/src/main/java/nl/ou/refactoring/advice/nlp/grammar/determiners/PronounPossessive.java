package nl.ou.refactoring.advice.nlp.grammar.determiners;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents a Possessive Pronoun in Natural Language.
 */
public final class PronounPossessive implements DeterminerWithToken {
	public static final long TOKEN = Tokens.Determiners.POSSESSIVE_PRONOUN;
	private final GrammaticalPerson person;
	private final GrammaticalGender gender;
	private final GrammaticalNumber number;
	
	/**
	 * Initialises a new instance of {@link PronounPossessive}.
	 */
	public PronounPossessive(GrammaticalPerson person, GrammaticalGender gender, GrammaticalNumber number) {
		this.person = person;
		this.gender = gender;
		this.number = number;
	}
	
	/**
	 * Gets the grammatical person of the possessive pronoun.
	 * @return The grammatical person of the possessive pronoun.
	 */
	public GrammaticalPerson getPerson() {
		return this.person;
	}
	
	/**
	 * Gets the grammatical gender of the possessive pronoun.
	 * @return The grammatical gender of the possessive pronoun.
	 */
	public GrammaticalGender getGender() {
		return this.gender;
	}
	
	/**
	 * Gets the grammatical number of the possessive pronoun.
	 * @return The grammatical number of the possessive pronoun.
	 */
	public GrammaticalNumber getNumber() {
		return this.number;
	}
	
	@Override
	public long getToken() {
		return TOKEN;
	}
}