package nl.ou.refactoring.advice.nlp.grammar.verbs;

/**
 * The voice of a verb.
 */
public enum VerbVoice {
	/**
	 * Subject performs the action.
	 */
	ACTIVE,
	
	/**
	 * Subject is directly affected by the action.
	 */
	PASSIVE,
	
	/**
	 * Subject acts on itself.
	 */
	MIDDLE,
	
	/**
	 * Subject and object are the same (often accompanied by a reflexive pronoun).
	 */
	REFLEXIVE
}
