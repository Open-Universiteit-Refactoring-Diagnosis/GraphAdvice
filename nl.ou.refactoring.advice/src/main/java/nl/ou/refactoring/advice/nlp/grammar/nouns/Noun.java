package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.SyntaxElement;

/**
 * Represents a Noun in Natural Language.
 */
public abstract class Noun implements Cloneable, SyntaxElement {
	private final long token;
	private final LexicalCategory lexicalCategory;
	private final SemanticClassification semanticClassification;
	private final Countability countability;
	private final Supplier<GrammaticalGender> genderSupplier;
	
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
		LexicalCategory lexicalCategory,
		SemanticClassification semanticClassification,
		Countability countability,
		Supplier<GrammaticalGender> genderSupplier
	) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(genderSupplier, "genderSupplier");
		this.token = token;
		this.lexicalCategory = lexicalCategory;
		this.semanticClassification = semanticClassification;
		this.countability = countability;
		this.genderSupplier = genderSupplier;
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
		return this.lexicalCategory;
	}
	
	/**
	 * Gets the Semantic Classification of the Noun.
	 * @return The Semantic Classification of the Noun.
	 */
	public SemanticClassification getSemanticClassification() {
		return this.semanticClassification;
	}
	
	/**
	 * Gets the Countability of the Noun.
	 * @return The Countability of the Noun.
	 */
	public Countability getCountability() {
		return this.countability;
	}
	
	/**
	 * Gets the Grammatical Gender of the Noun.
	 * @return The Grammatical Gender of the Noun.
	 */
	public GrammaticalGender getGender() {
		return this.genderSupplier.get();
	}
	
	/**
	 * Gets the supplier of the Grammatical Gender of the Noun.
	 * @return The supplier of the Grammatical Gender of the Noun.
	 */
	protected final Supplier<GrammaticalGender> getGenderSupplier() {
		return this.genderSupplier;
	}
	
	@Override
	protected abstract Object clone();
}