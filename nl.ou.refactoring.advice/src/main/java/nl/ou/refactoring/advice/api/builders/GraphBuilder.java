package nl.ou.refactoring.advice.api.builders;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.api.AdviceApi;
import nl.ou.refactoring.advice.api.RefactoringTemplateIdentifier;
import nl.ou.refactoring.advice.api.RefactoringTemplateUndefinedException;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflow;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddClass;

/**
 * Creates Refactoring Advice Graphs (RAG) with the builder pattern.
 */
public class GraphBuilder {
	/**
	 * The Refactoring Advice Graph (RAG) that is being built by the current
	 * builder.
	 */
	private final Graph graph;

	/**
	 * The last node in the Refactoring Advice Graph (RAG)'s workflow. Wrapped in
	 * {@link Optional}, empty if not present.
	 */
	private Optional<GraphNodeWorkflow> workflowLastNode;

	/**
	 * Initialises a new instance of {@link GraphBuilder}.
	 * 
	 * @param refactoringName The name of the refactoring.
	 * @throws ArgumentNullException  Thrown if refactoringName is null.
	 * @throws ArgumentEmptyException Thrown if refactoringName is an empty string
	 *                                or contains only white spaces.
	 */
	public GraphBuilder(String refactoringName) throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(refactoringName, "refactoringName");
		this.graph = AdviceApi.createRefactoringGraph(refactoringName);
		this.workflowLastNode = Optional.of(this.graph.start());
	}

	/**
	 * Initialises a new instance of {@link GraphBuilder}.
	 * 
	 * @param template The template from which to build a refactoring.
	 * @throws ArgumentNullException                 Thrown if template is null.
	 * @throws RefactoringTemplateUndefinedException Thrown if the template is
	 *                                               undefined.
	 */
	public GraphBuilder(RefactoringTemplateIdentifier template)
			throws ArgumentNullException, RefactoringTemplateUndefinedException {
		ArgumentGuard.requireNotNull(template, "template");
		this.graph = AdviceApi.createRefactoringGraphByTemplate(template);
		this.workflowLastNode = Optional.empty();
	}

	/**
	 * Initialises a new instance of {@link GraphBuilder}.
	 * 
	 * @param graph The Refactoring Advice Graph (RAG) that should be filled or
	 *              expanded.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphBuilder(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		this.graph = graph;
		if (this.graph.isFinalised()) {
			this.workflowLastNode = Optional.empty();
		} else {
			// TODO determine workflow last node
			this.workflowLastNode = Optional.empty();
		}
	}

	public GraphBuilderClassBuilder addClassWithMicrostep(String packageName, String className) {
		GraphNodeMicrostepAddClass microstepAddClass;
		if (this.graph.isFinalised()) {
			microstepAddClass =
					this.graph.getNodes(GraphNodeMicrostepAddClass.class)
							.stream()
							.findFirst()
							.orElseThrow(() -> new RuntimeException("TODO")); // TODO exception
			// TODO what if multiple "Add Class" microsteps?
		} else {
			microstepAddClass = new GraphNodeMicrostepAddClass(this.graph);
			this.workflowLastNode.ifPresentOrElse((lastNode) -> {
				switch (lastNode) {
					case GraphNodeRefactoringStart start: {
						start.initiates(microstepAddClass);
						break;
					}
					case GraphNodeMicrostep microstep: {
						microstep.precedes(microstepAddClass);
						break;
					}
					default: {
						throw new RuntimeException("TODO"); // TODO exception
					}
				}
			}, () -> {
				this.graph.getStart().ifPresentOrElse((startNode) -> {
					startNode.initiates(microstepAddClass);
				}, () -> {
					// TODO if no start node, create or throw exception?
				});
			});
			this.workflowLastNode = Optional.of(microstepAddClass);
		}

		final var classNode = AdviceApi.Code.computeClassSpecification(this.graph, packageName, className);
		microstepAddClass.adds(classNode);
		return new GraphBuilderClassBuilder(this, classNode);
	}

	/**
	 * Adds a Class specification to the Refactoring Advice Graph (RAG) if it is not
	 * defined yet, otherwise returns the existing specification.
	 * 
	 * @param packageName The name of the Package specification that should contain
	 *                    the Class specification.
	 * @param className   The name of the Class specification.
	 * @return The {@link GraphBuilderClassBuilder} that builds a Refactoring Advice
	 *         Graph (RAG) within a Class specification scope.
	 */
	public GraphBuilderClassBuilder computeClass(String packageName, String className) {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(packageName, "packageName");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(className, "className");
		final var classNode = AdviceApi.Code.computeClassSpecification(this.graph, packageName, className);
		return new GraphBuilderClassBuilder(this, classNode);
	}

	/**
	 * Gets the Refactoring Advice Graph (RAG) that is being built by the current
	 * builder.
	 * 
	 * @return The Refactoring Advice Graph (RAG) that is being built by the current
	 *         builder.
	 */
	public Graph getGraph() {
		return this.graph;
	}
}
