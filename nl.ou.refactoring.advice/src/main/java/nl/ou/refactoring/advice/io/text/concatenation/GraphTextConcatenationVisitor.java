package nl.ou.refactoring.advice.io.text.concatenation;

import java.io.PrintWriter;
import java.io.StringWriter;

import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.GraphPathSegment;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;

/**
 * Visits Refactoring Advice Graphs and generates a refactoring advice text by concatenating strings.
 */
public final class GraphTextConcatenationVisitor {
	/**
	 * Initialises a new instance of {@link GraphTextConcatenationVisitor}.
	 */
	public GraphTextConcatenationVisitor() {
	}
	
	/**
	 * Visits a Refactoring Advice Graph from its start node.
	 * @param startNode The start node of a Refactoring Advice Graph.
	 * @return The generated text.
	 * @throws ArgumentNullException Thrown if startNode is null.
	 */
	public String visit(GraphNodeRefactoringStart startNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(startNode, "startNode");
		final var stringWriter = new StringWriter();
		final var printWriter = new PrintWriter(stringWriter);
		final var refactoringName = startNode.getRefactoringName();
		printWriter.println(String.format("Refactoring: %s", refactoringName));
		
		final var workflowPaths = startNode.findPaths(startNode, 100);
		if (workflowPaths.size() == 0) {
			return stringWriter.toString();
		}
		final var workflowPath = workflowPaths.get(0);
		printWriter.print(this.visit(workflowPath));
		
		return stringWriter.toString();
	}
	
	private String visit(GraphPath path) {
		final var stringWriter = new StringWriter();
		
		for (final var segment : path.getSegments()) {
			stringWriter.write(this.visit(segment));
		}
		
		return stringWriter.toString();
	}
	
	private String visit(GraphPathSegment segment) {
		final var stringWriter = new StringWriter();
		
		stringWriter.write(switch (segment.getNode()) {
			case GraphNodeMicrostepAddMethod addMethod -> this.visit(addMethod);
			default -> "";
		});
		
		return stringWriter.toString();
	}
	
	private String visit(GraphNodeMicrostepAddMethod addMethod) {
		final var stringWriter = new StringWriter();
		final var printWriter = new PrintWriter(stringWriter);
		
		final var dangers = addMethod.getDangers();
		if (dangers.size() == 0) {
			return "";
		}
		printWriter.println(String.format("Adding method '%'"));
		
		return stringWriter.toString();
	}
}
