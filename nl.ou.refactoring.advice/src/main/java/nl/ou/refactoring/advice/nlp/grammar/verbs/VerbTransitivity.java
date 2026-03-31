package nl.ou.refactoring.advice.nlp.grammar.verbs;

/**
 * The transitivity of a verb.
 */
public enum VerbTransitivity {
	/**
	 * The verb requires a subject complement.
	 */
	COPULAR,
	
	/**
	 * The verb requires a direct object.
	 */
	TRANSITIVE,
	
	/**
	 * The verb does not take a direct object.
	 */
	INTRANSITIVE
}