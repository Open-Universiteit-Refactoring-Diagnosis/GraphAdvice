package nl.ou.refactoring.advice.nlp.tokens;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ou.refactoring.advice.nlp.grammar.nouns.CommonNoun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.Countability;
import nl.ou.refactoring.advice.nlp.grammar.nouns.SemanticClassification;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.Preposition;
import nl.ou.refactoring.advice.nlp.grammar.verbs.AuxiliaryVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.LexicalVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbTransitivity;

/**
 * Defines tokens for Natural Language Processing.
 */
public final class Tokens {
	private Tokens() { }
	
	/**
	 * Tokens for adjectives.
	 */
	public final class Adjectives {
		private Adjectives() { }
	}
	
	/**
	 * Tokens for Determiners.
	 */
	public final class Determiners {
		private Determiners() { }
		
		/**
		 * Tokens for Articles.
		 */
		public final class Articles {
			private Articles() { }
			
			/**
			 * The indeterminate article "a(n)".
			 */
			public static final long A = "a".hashCode();
			
			/**
			 * The determinate article "the".
			 */
			public static final long THE = "the".hashCode();
			
			/**
			 * Gets all tokens for articles.
			 * @return The tokens for articles.
			 */
			public static Set<Long> all() {
				return getAll(Articles.class);
			}
		}
		
		/**
		 * The token for Possessive Pronouns.
		 * Declension is left to grammar and language.
		 */
		public static final long POSSESSIVE_PRONOUN = "POSSESSIVE_PRONOUN".hashCode();
		
		/**
		 * Gets all tokens for Determiners.
		 * @return An unmodifiable set of tokens for determiners.
		 */
		public static Set<Long> all() {
			return
				Stream.concat(
					Articles.all().stream(),
					getAll(Determiners.class).stream()
				)
				.collect(Collectors.toUnmodifiableSet());
		}
	}
	
	/**
	 * Tokens for Nouns.
	 */
	public final class Nouns {
		private Nouns() { }
		
		/**
		 * Lexical tokens for Nouns.
		 */
		public final class Common {
			private Common() { }
			
			private static final TokenStore<CommonNoun> COMMON_NOUNS =
				new TokenStore<CommonNoun>();
			
			/**
			 * <a href="https://en.wiktionary.org/wiki/class">Class</a>;
			 * A set of objects having the same behavior (but typically differing in state), or a template defining such a set in terms of its common properties, functions, etc.
			 */
			public static final long CLASS_OO_PROGRAMMING = "class (OO PROGRAMMING)".hashCode();
			
			/**
			 * <a href="https://en.wiktionary.org/wiki/method">Method</a>:
			 * A process by which a task is completed; 
			 * a way of doing something (followed by the adposition of, to or for before the purpose of the process).
			 */
			public static final long METHOD = "method".hashCode();
			
			/**
			 * <a href="https://en.wiktionary.org/wiki/microstep">Microstep</a>:
			 * A very small step (typically made by a stepping motor).
			 */
			public static final long MICROSTEP = "microstep".hashCode();
			
			/**
			 * <a href="https://en.wiktionary.org/wiki/refactoring">Refactoring</a>:
			 * (programming) An act or process in which code is refactored.
			 */
			public static final long REFACTORING = "refactoring".hashCode();
			
			static {
				COMMON_NOUNS.putIfAbsent(
					CLASS_OO_PROGRAMMING,
					new CommonNoun(CLASS_OO_PROGRAMMING, SemanticClassification.ABSTRACT, Countability.COUNTABLE)
				);
				COMMON_NOUNS.putIfAbsent(
					METHOD,
					new CommonNoun(METHOD, SemanticClassification.ABSTRACT, Countability.COUNTABLE)
				);
				COMMON_NOUNS.putIfAbsent(
					MICROSTEP,
					new CommonNoun(MICROSTEP, SemanticClassification.ABSTRACT, Countability.COUNTABLE)
				);
				COMMON_NOUNS.putIfAbsent(
					REFACTORING,
					new CommonNoun(REFACTORING, SemanticClassification.ABSTRACT, Countability.COUNTABLE)
				);
			}
			
			/**
			 * Gets all dictionary noun tokens.
			 * @return An unmodifiable set of dictionary noun tokens.
			 */
			public static Set<Long> all() {
				return getAll(Common.class);
			}
			
			/**
			 * Gets the Common Noun with the specified token.
			 * @param token The token of the Common Noun.
			 * @return The Common Noun wrapped in {@link Optional}, otherwise an empty {@link Optional}.
			 */
			public static Optional<CommonNoun> get(long token) {
				return COMMON_NOUNS.get(token);
			}
		}
		
		/**
		 * A pronoun, considered a special case of a noun.
		 */
		public static final long PRONOUN = "PRONOUN".hashCode();
		
		/**
		 * A noun that references an object.
		 */
		public static final long REFERENCE = "REFERENCE_NOUN".hashCode();
		
		/**
		 * Gets all noun tokens.
		 * @return An unmodifiable set of noun tokens.
		 */
		public static Set<Long> all() {
			return
				Stream.concat(
					Common.all().stream(),
					getAll(Nouns.class).stream()
				)
				.collect(Collectors.toUnmodifiableSet());
		}
	}
	
	/**
	 * Tokens for Prepositions.
	 */
	public final class Prepositions {
		private Prepositions() { }
		
		private static final TokenStore<Preposition> PREPOSITIONS = new TokenStore<Preposition>();
		
		/**
		 * <a href="https://en.wiktionary.org/wiki/in">in</a>:
		 * Used to indicate location, inclusion, or position within spatial, temporal or abstract limits.
		 */
		public static final long IN = "in".hashCode();
		
		/**
		 * <a href="https://en.wiktionary.org/wiki/to">to</a>:
		 * 2. Indicating destination or final position: In the direction of, so as to arrive at or reach.
		 */
		public static final long TO_DIRECTIONAL = "to (directional)".hashCode();
		
		static {
			PREPOSITIONS.putIfAbsent(IN, new Preposition(IN));
			PREPOSITIONS.putIfAbsent(TO_DIRECTIONAL, new Preposition(TO_DIRECTIONAL));
		}
		
		/**
		 * Returns all preposition tokens.
		 * @return An unmodifiable set of preposition tokens.
		 */
		public static Set<Long> all() {
			return getAll(Prepositions.class);
		}
		
		/**
		 * Gets the Preposition with the specified token.
		 * @param token The token of the Preposition.
		 * @return The Preposition wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
		 */
		public static Optional<Preposition> get(long token) {
			return PREPOSITIONS.get(token);
		}
	}
	
	/**
	 * Verb tokens.
	 */
	public final class Verbs {
		private Verbs() { }
		
		/**
		 * Tokens for Auxiliary Verbs.
		 */
		public final class Auxiliary {
			private Auxiliary() { }
			
			private static final TokenStore<AuxiliaryVerb> AUXILIARY_VERBS = new TokenStore<AuxiliaryVerb>();
			
			/**
			 * The auxiliary verb "to be".
			 */
			public static final long BE = "to be (AUXILIARY)".hashCode();
			
			/**
			 * The auxiliary verb "to do".
			 */
			public static final long DO = "to do (AUXILIARY)".hashCode();
			
			/**
			 * The auxiliary verb "to have".
			 */
			public static final long HAVE = "to have (AUXILIARY)".hashCode();
			
			static {
				AUXILIARY_VERBS.putIfAbsent(BE, new AuxiliaryVerb(BE));
				AUXILIARY_VERBS.putIfAbsent(DO, new AuxiliaryVerb(DO));
				AUXILIARY_VERBS.putIfAbsent(HAVE, new AuxiliaryVerb(HAVE));
			}
			
			/**
			 * Gets all auxiliary verb tokens.
			 * @return An unmodifiable set of auxiliary verb tokens.
			 */
			public static Set<Long> all() {
				return getAll(Auxiliary.class);
			}
			
			/**
			 * Gets the Auxiliary Verb that is linked to the specified token.
			 * @param token The token of the Auxiliary Verb.
			 * @return The Auxiliary Verb that is linked to the specified token, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
			 */
			public static Optional<AuxiliaryVerb> get(long token) {
				return AUXILIARY_VERBS.get(token);
			}
		}
		
		/**
		 * Tokens for Main Verbs.
		 */
		public final class Lexical {
			private Lexical() { }
			
			private static TokenStore<LexicalVerb> LEXICAL_VERBS = new TokenStore<LexicalVerb>();
			
			/**
			 * To Add.
			 */
			public static final long ADD = "to add".hashCode();
			
			/**
			 * To Cause.
			 */
			public static final long CAUSE = "to cause".hashCode();
			
			/**
			 * To Have.
			 */
			public static final long HAVE = "to have (LEXICAL)".hashCode();
			
			/**
			 * To Refactor.
			 */
			public static final long REFACTOR = "to refactor".hashCode();
			
			/**
			 * To Remove.
			 */
			public static final long REMOVE = "to remove".hashCode();
			
			static {
				LEXICAL_VERBS.putIfAbsent(ADD, new LexicalVerb(ADD, VerbTransitivity.TRANSITIVE));
				LEXICAL_VERBS.putIfAbsent(CAUSE, new LexicalVerb(CAUSE, VerbTransitivity.TRANSITIVE));
				LEXICAL_VERBS.putIfAbsent(HAVE, new LexicalVerb(HAVE, VerbTransitivity.TRANSITIVE));
				LEXICAL_VERBS.putIfAbsent(REFACTOR, new LexicalVerb(REFACTOR, VerbTransitivity.TRANSITIVE));
				LEXICAL_VERBS.putIfAbsent(REMOVE, new LexicalVerb(REMOVE, VerbTransitivity.TRANSITIVE));
			}
			
			/**
			 * Gets all Lexical Verb tokens.
			 * @return An unmodifiable set of tokens for Lexical Verbs.
			 */
			public static Set<Long> all() {
				return getAll(Lexical.class);
			}
			
			/**
			 * Gets the {@link LexicalVerb} associated with the specified token.
			 * @param token The token that represents a {@link LexicalVerb}.
			 * @return The requested {@link LexicalVerb}, if registered wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
			 */
			public static Optional<LexicalVerb> get(long token) {
				return LEXICAL_VERBS.get(token);
			}
		}
		
		/**
		 * Tokens for Linking Verbs.
		 */
		public final class Linking {
			private Linking() { }
			
			/**
			 * The linking (copular) verb "to be".
			 */
			public static final long BE = "to be (LINKING)".hashCode();
			
			/**
			 * The linking (copular) verb "to become".
			 */
			public static final long BECOME = "to become (LINKING)".hashCode();
			
			/**
			 * Gets all tokens for Linking Verbs.
			 * @return An unmodifiable set of tokens for Linking Verbs.
			 */
			public static Set<Long> all() {
				return getAll(Lexical.class);
			}
		}
		
		/**
		 * Gets all verb tokens.
		 * @return An unmodifiable set of verb tokens.
		 */
		public static Set<Long> all() {
			final var tokensSet = new HashSet<Long>();
			tokensSet.addAll(Auxiliary.all());
			tokensSet.addAll(Lexical.all());
			tokensSet.addAll(Linking.all());
			return
				tokensSet
					.stream()
					.collect(Collectors.toUnmodifiableSet());
		}
	}
	
	/**
	 * Gets all tokens defined within a (static) class.
	 * @param classType The type of the specified (static) class.
	 * @return An unmodifiable set of tokens within the specified (static) class.
	 */
	private static Set<Long> getAll(Class<?> classType) {
		return
			Set.of(classType.getDeclaredFields())
				.stream()
				.filter(
					f -> {
						final var modifiers = f.getModifiers();
						return
							Modifier.isFinal(modifiers) &&
							Modifier.isStatic(modifiers);
					}
				)
				.map(f -> { try { return f.getLong(null); } catch (Exception e) { return (long)0; } })
				.collect(Collectors.toUnmodifiableSet());
	}
}