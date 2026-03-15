package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.LookupTree;
import nl.ou.refactoring.advice.nlp.LookupTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.grammar.determiners.PronounPossessive;

/**
 * A lookup tree for Possessive Pronoun declensions in the Dutch language (nl-NL).
 */
public final class PronounPossessiveLookupTree extends LookupTree<PronounPossessiveKey, Void, GrammaticalPerson> {
	/**
	 * The singleton instance of the lookup tree.
	 */
	public static final PronounPossessiveLookupTree INSTANCE = new PronounPossessiveLookupTree();
	
	private PronounPossessiveLookupTree() {
		super(constructTree());
	}
	
	private static LookupTreeNode<PronounPossessiveKey, Void, GrammaticalPerson> constructTree() {
		final var rootNode = new LookupTreeNode<PronounPossessiveKey, Void, GrammaticalPerson>(null, null);
		
		// First Person
		// First Person Singular
		final var mijnValue =
			new LookupTreeNode<PronounPossessiveKey, String, Void>("mijn", _ -> "mijn");
		final var firstPersonSingular =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalNumber, String>(GrammaticalNumber.SINGULAR, k -> k.number());
		firstPersonSingular.putIfAbsent(mijnValue);
		
		// First Person Plural
		final var onsValue =
			new LookupTreeNode<PronounPossessiveKey, String, Void>("ons", _ -> "ons");
		final var onzeValue =
			new LookupTreeNode<PronounPossessiveKey, String, Void>("onze", _ -> "onze");
		
		final var firstPersonPluralModifierNeuterSingular =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalNumber, String>(GrammaticalNumber.SINGULAR, k -> k.numberModifier());
		firstPersonPluralModifierNeuterSingular.putIfAbsent(onsValue);

		final var firstPersonPluralModifierNeuterPlural =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalNumber, String>(GrammaticalNumber.PLURAL, k -> k.numberModifier());
		firstPersonPluralModifierNeuterPlural.putIfAbsent(onzeValue);
		final var firstPersonPluralModifierNeuter =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalGender, GrammaticalNumber>(GrammaticalGender.NEUTER, k -> k.genderModifier());
		firstPersonPluralModifierNeuter.putIfAbsent(firstPersonPluralModifierNeuterSingular);
		firstPersonPluralModifierNeuter.putIfAbsent(firstPersonPluralModifierNeuterPlural);
		final var firstPersonPluralModifierFeminine =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalGender, String>(GrammaticalGender.FEMININE, k -> k.genderModifier());
		firstPersonPluralModifierFeminine.putIfAbsent(onzeValue);
		final var firstPersonPluralModifierMasculine =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalGender, String>(GrammaticalGender.MASCULINE, k -> k.genderModifier());
		firstPersonPluralModifierMasculine.putIfAbsent(onzeValue);
		
		final var firstPersonPlural =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalNumber, GrammaticalGender>(GrammaticalNumber.PLURAL, k -> k.number());
		firstPersonPlural.putIfAbsent(firstPersonPluralModifierFeminine);
		firstPersonPlural.putIfAbsent(firstPersonPluralModifierMasculine);
		firstPersonPlural.putIfAbsent(firstPersonPluralModifierNeuter);
					
		final var firstPerson =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalPerson, GrammaticalNumber>(GrammaticalPerson.FIRST, k -> k.person());
		firstPerson.putIfAbsent(firstPersonSingular);
		firstPerson.putIfAbsent(firstPersonPlural);
		rootNode.putIfAbsent(firstPerson);
		
		// Second Person
		// Second Person Singular
		final var jouwValue =
			new LookupTreeNode<PronounPossessiveKey, String, Void>("jouw", _ -> "jouw");
		final var secondPersonSingularCasual =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalRegister, String>(GrammaticalRegister.CASUAL, k -> k.register());
		secondPersonSingularCasual.putIfAbsent(jouwValue);
		final var secondPersonSingular =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalNumber, GrammaticalRegister>(GrammaticalNumber.SINGULAR, k -> k.number());
		secondPersonSingular.putIfAbsent(secondPersonSingularCasual);
		
		final var secondPerson =
			new LookupTreeNode<PronounPossessiveKey, GrammaticalPerson, GrammaticalNumber>(GrammaticalPerson.SECOND, k -> k.person());
		secondPerson.putIfAbsent(secondPersonSingular);
		rootNode.putIfAbsent(secondPerson);
		
		return rootNode;
	}
	
	/**
	 * Constructs a key from the specified possessive pronoun and the specified modifiers.
	 * @param possessivePronoun The possessive pronoun from which to construct a lookup key.
	 * @param genderModifier The grammatical gender to which the possessive pronoun must agree.
	 * @param numberModifier The grammatical number to which the possessive pronoun must agree.
	 * @param register The grammatical register to which the possessive pronoun must agree.
	 * @return The constructed lookup key.
	 * @throws ArgumentNullException Thrown if possessivePronoun is null.
	 */
	public static PronounPossessiveKey constructKey(
		PronounPossessive possessivePronoun,
		GrammaticalGender genderModifier,
		GrammaticalNumber numberModifier,
		GrammaticalRegister register
	) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(possessivePronoun, "possessivePronoun");
		return new PronounPossessiveKey(
			possessivePronoun.getPerson(),
			possessivePronoun.getGender(),
			possessivePronoun.getNumber(),
			genderModifier,
			numberModifier,
			register
		);
	}
}