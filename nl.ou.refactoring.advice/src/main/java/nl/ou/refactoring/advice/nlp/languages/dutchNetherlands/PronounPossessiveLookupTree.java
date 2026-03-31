package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.Set;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.LookupTree;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.grammar.determiners.PronounPossessive;
import nl.ou.refactoring.advice.nlp.grammar.determiners.PronounPossessiveKey;

/**
 * A lookup tree for Possessive Pronoun declensions in the Dutch language (nl-NL).
 */
public final class PronounPossessiveLookupTree
		extends LookupTree<PronounPossessiveKey, Void, GrammaticalPerson, PronounPossessiveLookupTreeNode<Void, GrammaticalPerson>> {
	/**
	 * The singleton instance of the lookup tree.
	 */
	public static final PronounPossessiveLookupTree INSTANCE = new PronounPossessiveLookupTree();
	
	private PronounPossessiveLookupTree() {
		super(constructTree());
	}
	
	private static PronounPossessiveLookupTreeNode<Void, GrammaticalPerson> constructTree() {
		final var rootNode = new PronounPossessiveLookupTreeNode<Void, GrammaticalPerson>(Set.of(), null);
		
		// First Person
		// First Person Singular
		final var mijnValue =
			new PronounPossessiveLookupTreeNode<String, Void>(Set.of("mijn"), _ -> "mijn");
		final var firstPersonSingular =
			new PronounPossessiveLookupTreeNode<GrammaticalNumber, String>(Set.of(GrammaticalNumber.SINGULAR), k -> k.number());
		firstPersonSingular.putIfAbsent(mijnValue);
		
		// First Person Plural
		final var onsValue =
			new PronounPossessiveLookupTreeNode<String, Void>(Set.of("ons"), _ -> "ons");
		final var onzeValue =
			new PronounPossessiveLookupTreeNode<String, Void>(Set.of("onze"), _ -> "onze");
		
		final var firstPersonPluralModifierNeuterSingular =
			new PronounPossessiveLookupTreeNode<GrammaticalNumber, String>(Set.of(GrammaticalNumber.SINGULAR), k -> k.numberModifier());
		firstPersonPluralModifierNeuterSingular.putIfAbsent(onsValue);

		final var firstPersonPluralModifierNeuterPlural =
			new PronounPossessiveLookupTreeNode<GrammaticalNumber, String>(Set.of(GrammaticalNumber.PLURAL), k -> k.numberModifier());
		firstPersonPluralModifierNeuterPlural.putIfAbsent(onzeValue);
		final var firstPersonPluralModifierNeuter =
			new PronounPossessiveLookupTreeNode<GrammaticalGender, GrammaticalNumber>(Set.of(GrammaticalGender.NEUTER), k -> k.genderModifier());
		firstPersonPluralModifierNeuter.putIfAbsent(firstPersonPluralModifierNeuterSingular);
		firstPersonPluralModifierNeuter.putIfAbsent(firstPersonPluralModifierNeuterPlural);
		final var firstPersonPluralModifierFeminine =
			new PronounPossessiveLookupTreeNode<GrammaticalGender, String>(Set.of(GrammaticalGender.FEMININE), k -> k.genderModifier());
		firstPersonPluralModifierFeminine.putIfAbsent(onzeValue);
		final var firstPersonPluralModifierMasculine =
			new PronounPossessiveLookupTreeNode<GrammaticalGender, String>(Set.of(GrammaticalGender.MASCULINE), k -> k.genderModifier());
		firstPersonPluralModifierMasculine.putIfAbsent(onzeValue);
		
		final var firstPersonPlural =
			new PronounPossessiveLookupTreeNode<GrammaticalNumber, GrammaticalGender>(Set.of(GrammaticalNumber.PLURAL), k -> k.number());
		firstPersonPlural.putIfAbsent(firstPersonPluralModifierFeminine);
		firstPersonPlural.putIfAbsent(firstPersonPluralModifierMasculine);
		firstPersonPlural.putIfAbsent(firstPersonPluralModifierNeuter);
					
		final var firstPerson =
			new PronounPossessiveLookupTreeNode<GrammaticalPerson, GrammaticalNumber>(Set.of(GrammaticalPerson.FIRST), k -> k.person());
		firstPerson.putIfAbsent(firstPersonSingular);
		firstPerson.putIfAbsent(firstPersonPlural);
		rootNode.putIfAbsent(firstPerson);
		
		// Second Person
		// Second Person Singular
		final var jouwValue =
			new PronounPossessiveLookupTreeNode<String, Void>(Set.of("jouw"), _ -> "jouw");
		final var secondPersonSingularCasual =
			new PronounPossessiveLookupTreeNode<GrammaticalRegister, String>(Set.of(GrammaticalRegister.CASUAL), k -> k.register());
		secondPersonSingularCasual.putIfAbsent(jouwValue);
		final var secondPersonSingular =
			new PronounPossessiveLookupTreeNode<GrammaticalNumber, GrammaticalRegister>(Set.of(GrammaticalNumber.SINGULAR), k -> k.number());
		secondPersonSingular.putIfAbsent(secondPersonSingularCasual);
		
		final var secondPerson =
			new PronounPossessiveLookupTreeNode<GrammaticalPerson, GrammaticalNumber>(Set.of(GrammaticalPerson.SECOND), k -> k.person());
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