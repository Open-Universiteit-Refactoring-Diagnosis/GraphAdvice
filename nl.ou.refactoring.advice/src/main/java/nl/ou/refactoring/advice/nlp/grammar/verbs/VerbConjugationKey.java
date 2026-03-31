package nl.ou.refactoring.advice.nlp.grammar.verbs;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;

/**
 * A key for looking up a conjugation of a verb.
 * @param person The {@link GrammaticalPerson} of the verb.
 * @param number The {@link GrammaticalNumber} of the verb.
 * @param aspect The {@link VerbAspect} of the verb.
 * @param modality The {@link VerbModality} of the verb.
 * @param tense The {@link VerbTense} of the verb.
 * @param voice The {@link VerbVoice} of the verb.
 * @param register The {@link GrammaticalRegister} of the verb.
 */
public final record VerbConjugationKey
(
	GrammaticalPerson person,
	GrammaticalNumber number,
	VerbAspect aspect,
	VerbModality modality,
	VerbTense tense,
	VerbVoice voice,
	GrammaticalRegister register
) {
	/**
	 * The default conjugation for {@link Verb}s.
	 */
	public static final VerbConjugationKey DEFAULT =
		new VerbConjugationKey(
			GrammaticalPerson.THIRD,
			GrammaticalNumber.SINGULAR,
			VerbAspect.IMPERFECTIVE,
			VerbModality.INDICATIVE,
			VerbTense.PRESENT,
			VerbVoice.ACTIVE,
			GrammaticalRegister.PLAIN
		);
	
	/**
	 * Creates a copy of this {@link VerbConjugationKey} but with the specified {@link VerbVoice}.
	 * @param voice The new {@link VerbVoice}.
	 * @return A copy of this {@link VerbConjugationKey} but with the specified {@link VerbVoice}.
	 */
	public VerbConjugationKey withVoice(VerbVoice voice) {
		return
			new VerbConjugationKey(
				this.person,
				this.number,
				this.aspect,
				this.modality,
				this.tense,
				voice,
				this.register
			);
	}
}