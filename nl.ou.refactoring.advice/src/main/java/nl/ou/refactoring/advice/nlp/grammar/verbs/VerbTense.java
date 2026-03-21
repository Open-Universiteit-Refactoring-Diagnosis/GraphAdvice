package nl.ou.refactoring.advice.nlp.grammar.verbs;

/**
 * The tense of a verb.
 */
public enum VerbTense {
	/**
	 * Happening now.
	 */
	PRESENT,
	
	/**
	 * Completed but presently relevant.
	 */
	PRESENT_PERFECT,
	
	/**
	 * Happened in the past.
	 */
	PAST,
	
	/**
	 * Completed in the past and no longer directly adjacent to the present.
	 */
	PAST_PERFECT,
	
	/**
	 * Happens in the future.
	 */
	FUTURE,
	
	/**
	 * Is completed before a future point in time.
	 */
	FUTURE_PERFECT
}
