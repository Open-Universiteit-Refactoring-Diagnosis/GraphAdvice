package nl.ou.refactoring.advice.nlp.grammar.verbs;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;

/**
 * A key for looking up a conjugation of a verb.
 */
public final record VerbConjugationKey
(
	GrammaticalPerson person,
	GrammaticalNumber number,
	VerbAspect aspect,
	VerbModality modality,
	VerbTense tense,
	VerbVoice voice
) { }
