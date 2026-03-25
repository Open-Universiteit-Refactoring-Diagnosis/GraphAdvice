package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Optional;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.grammar.HasGenderAgreement;
import nl.ou.refactoring.advice.nlp.grammar.HasNumberAgreement;
import nl.ou.refactoring.advice.nlp.grammar.HasRegisterAgreement;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents a Pronoun in Natural Language.
 */
public final class Pronoun
		extends Noun
		implements
			HasGenderAgreement,
			HasNumberAgreement,
			HasRegisterAgreement {
	/**
	 * The token for a Pronoun.
	 */
	public static long TOKEN = Tokens.Nouns.PRONOUN;
	private final GrammaticalPerson person;
	private final GrammaticalGender gender;
	private final GrammaticalNumber number;
	private GrammaticalGender genderAgreement;
	private GrammaticalNumber numberAgreement;
	private GrammaticalRegister registerAgreement;
	
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
	
	@Override
	public GrammaticalGender getGenderAgreement() {
		return this.genderAgreement;
	}

	@Override
	public void setGenderAgreement(GrammaticalGender gender) {
		this.genderAgreement = gender;
	}
	
	@Override
	public GrammaticalNumber getNumberAgreement() {
		return this.numberAgreement;
	}

	@Override
	public void setNumberAgreement(GrammaticalNumber number) {
		this.numberAgreement = number;
	}
	
	@Override
	public GrammaticalRegister getRegisterAgreement() {
		return this.registerAgreement;
	}
	
	@Override
	public void setRegisterAgreement(GrammaticalRegister register) {
		this.registerAgreement = register;
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