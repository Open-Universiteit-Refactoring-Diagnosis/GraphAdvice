package nl.ou.refactoring.advice.nlp.tokens;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.nouns.CommonNoun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.Countability;
import nl.ou.refactoring.advice.nlp.grammar.nouns.SemanticClassification;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.Preposition;
import nl.ou.refactoring.advice.nlp.grammar.verbs.AuxiliaryVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.LexicalVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.Verb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
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
			public static final long A = "[article] a".hashCode();
			
			/**
			 * The determinate article "the".
			 */
			public static final long THE = "[article] the".hashCode();
			
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
			 * <a href="https://en.wiktionary.org/wiki/class">Class</a>:
			 * A set of objects having the same behavior (but typically differing in state), or a template defining such a set in terms of its common properties, functions, etc.
			 */
			public static final long CLASS_OO_PROGRAMMING = "[common noun] class (OO programming)".hashCode();
			
			/**
			 * <a href="https://en.wiktionary.org/wiki/field">Field</a>:
			 * A physical or virtual location for the input of information in the form of symbols.
			 */
			public static final long FIELD_OO_PROGRAMMING = "[common noun] field (OO programming)".hashCode();
			
			/**
			 * <a href="https://en.wiktionary.org/wiki/method">Method</a>:
			 * A process by which a task is completed; 
			 * a way of doing something (followed by the adposition of, to or for before the purpose of the process).
			 */
			public static final long METHOD = "[common noun] method".hashCode();
			
			/**
			 * <a href="https://en.wiktionary.org/wiki/microstep">Microstep</a>:
			 * A very small step (typically made by a stepping motor).
			 */
			public static final long MICROSTEP = "[common noun] microstep".hashCode();
			
			/**
			 * <a href="https://en.wiktionary.org/wiki/refactoring">Refactoring</a>:
			 * (programming) An act or process in which code is refactored.
			 */
			public static final long REFACTORING = "[common noun] refactoring".hashCode();
			
			static {
				COMMON_NOUNS.computeIfAbsent(
					CLASS_OO_PROGRAMMING,
					new CommonNoun(CLASS_OO_PROGRAMMING, SemanticClassification.ABSTRACT, Countability.COUNTABLE)
				);
				COMMON_NOUNS.computeIfAbsent(
					METHOD,
					new CommonNoun(METHOD, SemanticClassification.ABSTRACT, Countability.COUNTABLE)
				);
				COMMON_NOUNS.computeIfAbsent(
					MICROSTEP,
					new CommonNoun(MICROSTEP, SemanticClassification.ABSTRACT, Countability.COUNTABLE)
				);
				COMMON_NOUNS.computeIfAbsent(
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
		 * <a href="https://en.wiktionary.org/wiki/from">from</a>:
		 * Indicating removal or separation.
		 */
		public static final long FROM_REMOVAL_SEPARATION = "[preposition] from (removal or separation)".hashCode();
		
		/**
		 * <a href="https://en.wiktionary.org/wiki/in">in</a>:
		 * Used to indicate location, inclusion, or position within spatial, temporal or abstract limits.
		 */
		public static final long IN = "[preposition] in".hashCode();
		
		/**
		 * <a href="https://en.wiktionary.org/wiki/to">to</a>:
		 * 3. Used to indicate the target or recipient of an action.
		 */
		public static final long TO_TARGET_RECIPIENT = "[preposition] to (target or recipient)".hashCode();
		
		static {
			PREPOSITIONS.computeIfAbsent(FROM_REMOVAL_SEPARATION, new Preposition(FROM_REMOVAL_SEPARATION));
			PREPOSITIONS.computeIfAbsent(IN, new Preposition(IN));
			PREPOSITIONS.computeIfAbsent(TO_TARGET_RECIPIENT, new Preposition(TO_TARGET_RECIPIENT));
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
			
			private static final TokenStore<Function<Verb, AuxiliaryVerb>> AUXILIARY_VERBS =
				new TokenStore<Function<Verb, AuxiliaryVerb>>();
			
			/**
			 * The auxiliary verb "to be".
			 */
			public static final long BE = "[auxiliary verb] to be".hashCode();
			
			/**
			 * The auxiliary verb "to become". (e.g. "worden" in "wordt gezien")
			 */
			public static final long BECOME = "[auxiliary verb] to become".hashCode();
			
			/**
			 * The auxiliary verb "to do".
			 */
			public static final long DO = "[auxiliary verb] to do".hashCode();
			
			/**
			 * The auxiliary verb "to have".
			 */
			public static final long HAVE = "[auxiliary verb] to have".hashCode();
			
			static {
				AUXILIARY_VERBS.computeIfAbsent(BE, (headVerb) -> new AuxiliaryVerb(BE, headVerb));
				AUXILIARY_VERBS.computeIfAbsent(BECOME, (headVerb) -> new AuxiliaryVerb(BECOME, headVerb));
				AUXILIARY_VERBS.computeIfAbsent(DO, (headVerb) -> new AuxiliaryVerb(DO, headVerb));
				AUXILIARY_VERBS.computeIfAbsent(HAVE, (headVerb) -> new AuxiliaryVerb(HAVE, headVerb));
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
			 * @param mainVerb The main verb of the Auxiliary Verb.
			 * @return The Auxiliary Verb that is linked to the specified token, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
			 */
			public static Optional<AuxiliaryVerb> get(long token, Verb mainVerb)
					throws ArgumentNullException {
				ArgumentGuard.requireNotNull(mainVerb, "mainVerb");
				return AUXILIARY_VERBS.get(token).map(factory -> factory.apply(mainVerb));
			}
		}
		
		/**
		 * Tokens for Main Verbs.
		 */
		public final class Lexical {
			private Lexical() { }
			
			private static TokenStore<Function<VerbPhrase, Verb>> LEXICAL_VERBS =
				new TokenStore<Function<VerbPhrase, Verb>>();
			
			/**
			 * To Add.
			 */
			public static final long ADD = "[lexical verb] to add".hashCode();
			
			/**
			 * To Cause.
			 */
			public static final long CAUSE = "[lexical verb] to cause".hashCode();
			
			/**
			 * To Have.
			 */
			public static final long HAVE = "[lexical verb] to have".hashCode();
			
			/**
			 * To Refactor.
			 */
			public static final long REFACTOR = "[lexical verb] to refactor".hashCode();
			
			/**
			 * To Remove.
			 */
			public static final long REMOVE = "[lexical verb] to remove".hashCode();
			
			static {
				LEXICAL_VERBS.computeIfAbsent(ADD, phrase -> new LexicalVerb(ADD, VerbTransitivity.TRANSITIVE, phrase));
				LEXICAL_VERBS.computeIfAbsent(CAUSE, phrase -> new LexicalVerb(CAUSE, VerbTransitivity.TRANSITIVE, phrase));
				LEXICAL_VERBS.computeIfAbsent(HAVE, phrase -> new LexicalVerb(HAVE, VerbTransitivity.TRANSITIVE, phrase));
				LEXICAL_VERBS.computeIfAbsent(REFACTOR, phrase -> new LexicalVerb(REFACTOR, VerbTransitivity.TRANSITIVE, phrase));
				LEXICAL_VERBS.computeIfAbsent(REMOVE, phrase -> new LexicalVerb(REMOVE, VerbTransitivity.TRANSITIVE, phrase));
			}
			
			/**
			 * Gets all Lexical Verb tokens.
			 * @return An unmodifiable set of tokens for Lexical Verbs.
			 */
			public static Set<Long> all() {
				return getAll(Lexical.class);
			}
			
			/**
			 * Gets a factory function for the {@link LexicalVerb} associated with the specified token.
			 * @param token The token that represents a {@link LexicalVerb}.
			 * @return If found, the requested factory function for {@link LexicalVerb} wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
			 */
			public static Optional<Function<VerbPhrase, Verb>> get(long token) {
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
			public static final long BE = "[linking verb] to be".hashCode();
			
			/**
			 * The linking (copular) verb "to become".
			 */
			public static final long BECOME = "[linking verb] to become".hashCode();
			
			/**
			 * Gets all tokens for Linking Verbs.
			 * @return An unmodifiable set of tokens for Linking Verbs.
			 */
			public static Set<Long> all() {
				return getAll(Linking.class);
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