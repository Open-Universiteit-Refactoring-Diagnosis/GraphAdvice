package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Optional;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.SyntaxElement;

/**
 * Represents a Noun in Natural Language.
 */
public abstract class Noun
		implements
			Cloneable,
			SyntaxElement {
	private final long token;
	protected final NounCategories categories;
	
	/**
	 * Initialises a new instance of {@link Noun}.
	 * @param lexicalCategory The lexical category of the Noun.
	 * @param semanticClassification The semantic classification of the Noun.
	 * @param countability The countability of the Noun.
	 * @param genderSupplier Supplies a grammatical gender. Each language may designate a different grammatical gender to a Noun.
	 * @throws ArgumentNullException Thrown if genderProvider is null.
	 */
	protected Noun(
		long token,
		NounCategories categories
	) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(categories, "categories");
		this.token = token;
		this.categories = categories;
	}
	
	/**
	 * Gets the token that represents the Noun.
	 * @return The token that represents the Noun.
	 */
	public final long getToken() {
		return this.token;
	}
	
	/**
	 * Gets the Lexical Category of the Noun.
	 * @return The Lexical Category of the Noun.
	 */
	public final LexicalCategory getLexicalCategory() {
		return this.categories.lexicalCategory();
	}
	
	/**
	 * Gets the Semantic Classification of the Noun.
	 * @return The Semantic Classification of the Noun.
	 */
	public SemanticClassification getSemanticClassification() {
		return this.categories.semanticClassification();
	}
	
	/**
	 * Gets the Countability of the Noun.
	 * @return The Countability of the Noun.
	 */
	public Countability getCountability() {
		return this.categories.countability();
	}
	
	/**
	 * Gets the grammatical gender of the Noun.
	 * Each Noun class may have a different way of determining the gender.
	 * For example, a Pronoun's gender is intrinsic, a Common Noun's gender is determined by the language.
	 * @return The grammatical gender of the Noun if found, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 * @throws ArgumentNullException Thrown if genderSupplier is null.
	 */
	public abstract Optional<GrammaticalGender> getGender(Supplier<GrammaticalGender> genderSupplier)
		throws ArgumentNullException;
	
	@Override
	protected abstract Object clone();
}