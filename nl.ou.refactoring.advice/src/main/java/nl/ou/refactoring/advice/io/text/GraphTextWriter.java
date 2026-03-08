package nl.ou.refactoring.advice.io.text;

import java.io.StringWriter;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphStringWriter;
import nl.ou.refactoring.advice.io.GraphWriterException;
import nl.ou.refactoring.advice.nlp.NLPProvider;

/**
 * Base class for generating text from Refactoring Advice Graphs.
 */
public final class GraphTextWriter extends GraphStringWriter {
	private final NLPProvider nlpProvider;
	
	/**
	 * Initialises a new instance of {@link GraphTextWriter}.
	 * @param stringWriter The {@link StringWriter} responsible for text output.
	 */
	public GraphTextWriter(StringWriter stringWriter, NLPProvider nlpProvider) {
		super(stringWriter);
		ArgumentGuard.requireNotNull(nlpProvider, "nlpProvider");
		this.nlpProvider = nlpProvider;
	}
	
	@Override
	public void write(Graph graph)
		throws
			ArgumentNullException,
			GraphValidationException,
			GraphWriterException {
		final var nlpResult = this.nlpProvider.process(graph);
		var resultText = nlpResult.getText();
		for (final var entry : nlpResult.getReferences().entrySet()) {
			final var reference = entry.getKey();
			final var node = entry.getValue();
			resultText = resultText.replace(reference, node.getCaption());
		}
		this.print(resultText);
	}
}