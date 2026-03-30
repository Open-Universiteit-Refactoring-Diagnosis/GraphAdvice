package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Optional;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents a Pronoun in Natural Language.
 */
public final class Pronoun
		extends Noun {
	/**
	 * The token for a Pronoun.
	 */
	public static long TOKEN = Tokens.Nouns.PRONOUN;
	private final GrammaticalPerson person;
	private final GrammaticalGender gender;
	private final GrammaticalNumber number;
	private NounDeclensionKey declension;
	
	/**
	 * Initialises a new instance of {@link Pronoun}.
	 */
	public Pronoun(GrammaticalPerson person, GrammaticalGender gender, GrammaticalNumber number) {
		super(
			TOKEN,
			new NounCategories(
				LexicalCategory.PROPER,
				SemanticClassification.CONCRETE,
				Countability.UNCOUNTABLE
			)
		);
		this.person = person;
		this.gender = gender;
		this.number = number;
	}
	
	/**
	 * Gets the grammatical person of the pronoun.
	 * @return The grammatical person of the pronoun.
	 */
	public GrammaticalPerson getPerson() {
		return this.person;
	}
	
	@Override
	public Optional<GrammaticalGender> getGender(Supplier<GrammaticalGender> genderSupplier)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(genderSupplier, "genderSupplier");
		return Optional.of(this.gender);
	}
	
	/**
	 * Gets the grammatical number of the pronoun.
	 * @return The grammatical number of the pronoun.
	 */
	public GrammaticalNumber getNumber() {
		return this.number;
	}
	
	/**
	 * Gets the declension of the {@link Pronoun}.
	 * @return The declension of the {@link Pronoun}.
	 */
	public NounDeclensionKey getDeclension() {
		return this.declension;
	}
	
	/**
	 * Sets the declension of the {@link Pronoun}.
	 * @param declension The declension of the {@link Pronoun}.
	 * @throws ArgumentNullException Thrown if declension is null.
	 */
	void setDeclension(NounDeclensionKey declension)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(declension, "declension");
		this.declension = declension;
	}
	
	@Override
	protected Object clone() {
		return new Pronoun(this.person, this.gender, this.number);
	}
	
	@Override
	public String toString() {
		return "Pronoun " + this.getToken();
	}
}