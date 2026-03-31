package nl.ou.refactoring.advice.nlp.languages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Manages and provides available languages for Natural Language Processing.
 */
public final class NLPLanguages {
	private static final Map<Locale, NLPLanguage> LANGUAGES_MAP =
		new HashMap<Locale, NLPLanguage>();

	private NLPLanguages() { }
	
	/**
	 * Gets the {@link NLPLanguage} specified by locale, wrapped in an {@link Optional<NLPLanguage>} or an empty {@link Optional<NLPLanguage>} if no language is registered for the specified locale.
	 * @param locale The locale of the language.
	 * @return The {@link NLPLanguage} specified by locale, wrapped in an {@link Optional<NLPLanguage>} or an empty {@link Optional<NLPLanguage>} if no language is registered for the specified locale.
	 * @throws ArgumentNullException Thrown if locale is null.
	 */
	public static Optional<NLPLanguage> get(Locale locale)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(locale, "locale");
		return
			LANGUAGES_MAP.containsKey(locale)
				? Optional.of(LANGUAGES_MAP.get(locale))
				: Optional.empty();
	}

	/**
	 * Registers a language for Natural Language Processing.
	 * @param locale The locale of the language to register.
	 * @param language The language to register.
	 * @throws ArgumentNullException Thrown if locale or language is null.
	 */
	public static void register(Locale locale, NLPLanguage language)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(locale, "locale");
		ArgumentGuard.requireNotNull(language, "language");
		LANGUAGES_MAP.putIfAbsent(locale, language);
	}
}