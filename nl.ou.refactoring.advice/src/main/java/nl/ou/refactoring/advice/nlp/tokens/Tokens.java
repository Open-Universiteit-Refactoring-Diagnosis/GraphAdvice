package nl.ou.refactoring.advice.nlp.tokens;

/**
 * Defines tokens for Natural Language Processing.
 */
public final class Tokens {
	private Tokens() { }
	
	public final class Adjectives {
		private Adjectives() { }
		public static final long AMAZING = "amazing".hashCode();
		public static final long WONDERFUL = "wonderful".hashCode();
	}
	
	public final class Determiners {
		private Determiners() { }
		public static final long A = "a".hashCode();
		public static final long POSSESSIVE_PRONOUN = "POSSESSIVE_PRONOUN".hashCode();
		public static final long THE = "the".hashCode();
	}
	
	public final class Nouns {
		private Nouns() { }
		public static final long MICROSTEP = "microstep".hashCode();
		public static final long REFACTORING = "refactoring".hashCode();
	}
	
	public final class Verbs {
		private Verbs() { }
		public static final long ADD = "to add".hashCode();
		public static final long BE = "to be".hashCode();
		public static final long HAVE = "to have".hashCode();
		public static final long REFACTOR = "to refactor".hashCode();
	}
}