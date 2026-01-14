package nl.ou.refactoring.advice.io.text.concatenation;

import java.io.StringWriter;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.text.GraphTextWriter;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMustContainStartNodeException;

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
			throws ArgumentNullException, RefactoringMustContainStartNodeException {
		final var startNode = graph.getStart();
		if (startNode == null) {
			throw new RefactoringMustContainStartNodeException();
		}
		
		final var visitor = new GraphTextConcatenationVisitor();
		this.printLine(visitor.visit(startNode));
	}
}
