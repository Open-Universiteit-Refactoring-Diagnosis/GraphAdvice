package nl.ou.refactoring.advice.io.text.concatenation;

import java.io.StringWriter;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriterException;
import nl.ou.refactoring.advice.io.text.GraphTextWriter;
import nl.ou.refactoring.advice.nlp.providers.NLPConcatenationProvider;

/**
 * Generates refactoring advice text from a Refactoring Advice Graph by concatenating the text.
 */
public final class GraphTextConcatenationWriter extends GraphTextWriter {
	/**
	 * Initialises a new instance of {@link GraphTextConcatenationWriter}.
	 * @param stringWriter The {@link StringWriter} that is responsible for text output.
	 */
	public GraphTextConcatenationWriter(StringWriter stringWriter) {
		super(stringWriter);
	}

	@Override
	public void write(Graph graph)
			throws
				ArgumentNullException,
				GraphValidationException,
				GraphWriterException {
		final var nlpConcatenationProvider = new NLPConcatenationProvider();
		final var nlpResult = nlpConcatenationProvider.process(graph);
		var resultText = nlpResult.getText();
		for (final var entry : nlpResult.getReferences().entrySet()) {
			final var reference = entry.getKey();
			final var node = entry.getValue();
			resultText = resultText.replace(reference, node.getCaption());
		}
		this.print(resultText);
	}
}