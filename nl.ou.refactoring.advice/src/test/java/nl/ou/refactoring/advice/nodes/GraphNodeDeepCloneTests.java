package nl.ou.refactoring.advice.nodes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeBlock;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeExpressionStatement;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

/**
 * Tests for deep cloning of Refactoring Advice Graph (RAGs).
 */
public final class GraphNodeDeepCloneTests {
	@DisplayName("Should deep clone a Refactoring Advice Graph (RAG)")
	@Test
	public void deepCloneTest() {
		final var graph = new Graph("Clone test");
		
		// AST
		GraphNodePackage.parse(graph, "nl.ou.refactoring.test");
		final var packageNodeLeaf = graph.getNode("nl.ou.refactoring.test", GraphNodePackage.class).get();
		final var classNodeLegacyEmployee = new GraphNodeClass(graph, new GraphNodeIdentifier(graph, "LegacyEmployee"));
		
		// AST: Employee
		final var classNodeEmployee = new GraphNodeClass(graph, new GraphNodeIdentifier(graph, "Employee"));
		final var operationNodeEmployeeGetName = new GraphNodeOperation(graph, new GraphNodeIdentifier(graph, "getName"));
		classNodeEmployee.has(operationNodeEmployeeGetName);
		
		// AST: Company
		final var classNodeCompany = new GraphNodeClass(graph, new GraphNodeIdentifier(graph, "Company"));
		final var attributeNodeCompanyDirector = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "director"));
		classNodeCompany.has(attributeNodeCompanyDirector);
		final var operationNodeCompanyGetDirectorName = new GraphNodeOperation(graph, new GraphNodeIdentifier(graph, "getDirectorName"));
		final var blockNodeCompanyGetDirectorName = new GraphNodeBlock(graph);
		operationNodeCompanyGetDirectorName.hasBody(blockNodeCompanyGetDirectorName);
		final var methodInvocationExpression = new GraphNodeMethodInvocationExpression(graph);
		methodInvocationExpression.invokes(operationNodeEmployeeGetName);
		final var statementExpression = new GraphNodeExpressionStatement(graph);
		statementExpression.has(methodInvocationExpression);
		blockNodeCompanyGetDirectorName.has(statementExpression);
		classNodeCompany.has(operationNodeCompanyGetDirectorName);
		
		packageNodeLeaf.has(classNodeLegacyEmployee);
		packageNodeLeaf.has(classNodeEmployee);
		classNodeEmployee.is(classNodeLegacyEmployee);
		packageNodeLeaf.has(classNodeCompany);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		startNode.initiates(addMethod);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(removeMethod);
		removeMethod.finalises();
		
		final var graphClone = (Graph)graph.clone(graph.getRefactoringName() + " (Cloned)");
		assertEquals("Clone test (Cloned)", graphClone.getRefactoringName());
		final var graphNodesCloned = graphClone.getNodes();
		// assertEquals(graph.getNodes().size(), graphNodesCloned.size());
		final var graphEdgesCloned = graphClone.getEdges();
		// assertEquals(graph.getEdges().size(), graphEdgesCloned.size());
	}
}
