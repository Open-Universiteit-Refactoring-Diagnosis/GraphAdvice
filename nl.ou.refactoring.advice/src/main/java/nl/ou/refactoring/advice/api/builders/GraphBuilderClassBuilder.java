package nl.ou.refactoring.advice.api.builders;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

/**
 * Builds the Abstract Syntax Tree (AST) subgraph in a Refactoring Advice Graph
 * (RAG) with a new Class.
 */
final class GraphBuilderClassBuilder extends GraphBuilder {
	private final GraphBuilder builder;
	private final GraphNodeClass classNode;

	GraphBuilderClassBuilder(GraphBuilder builder, GraphNodeClass classNode) throws ArgumentNullException {
		super(builder.getGraph());
		ArgumentGuard.requireNotNull(builder, "builder");
		ArgumentGuard.requireNotNull(classNode, "classNode");
		this.builder = builder;
		this.classNode = classNode;
	}
	
	// TODO add fields
	// TODO add methods
	// TODO modify class
}
