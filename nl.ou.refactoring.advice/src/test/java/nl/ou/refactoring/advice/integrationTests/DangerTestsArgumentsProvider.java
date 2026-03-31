package nl.ou.refactoring.advice.integrationTests;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeBlock;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeExpressionStatement;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskChangedNestedRelationship;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskForcedOverride;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskImposedSpecification;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskPrecedingOverload;

public final class DangerTestsArgumentsProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) throws RefactoringMayContainOnlyOneStartNodeException {
		return
				Stream.of(
						constructDoubleDefinitionGraph(),
						constructForcedOverrideGraph(),
						constructImposedSpecificationGraph(),
						constructPrecedingOverloadGraph(),
						constructChangedNestedRelationship()
				)
				.map(Arguments::of);
	}
	
	private static Graph constructDoubleDefinitionGraph()
			throws RefactoringMayContainOnlyOneStartNodeException {
		final var graph = new Graph("AM-1 Double Definition");
		
		// Code
		final var packageNode = GraphNodePackage.parse(graph, "nl.ou.refactoring.doubleDefinition");
		final var alphaIdentifier = new GraphNodeIdentifier(graph, "Alpha");
		final var alphaClassNode =
				new GraphNodeClass(
						graph,
						alphaIdentifier
				);
		packageNode.has(alphaClassNode);
		final var fooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var fooOperationNode = new GraphNodeOperation(graph, fooIdentifier);
		alphaClassNode.has(fooOperationNode);

		final var fooNewOperationNode = new GraphNodeOperation(graph, fooIdentifier);
		alphaClassNode.has(fooNewOperationNode);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		addMethodNode.adds(fooNewOperationNode);
		startNode.initiates(addMethodNode);
		final var doubleDefinitionRisk = new GraphNodeRiskDoubleDefinition(graph);
		doubleDefinitionRisk.affects(fooOperationNode);
		doubleDefinitionRisk.affects(fooNewOperationNode);
		addMethodNode.causes(doubleDefinitionRisk);
		
		return graph;
	}
	
	private static Graph constructForcedOverrideGraph()
			throws RefactoringMayContainOnlyOneStartNodeException {
		final var graph = new Graph("AM-2 Forced Override");
		
		// Code
		final var packageNode = GraphNodePackage.parse(graph, "nl.ou.refactoring.dangers.forcedOverride");
		
		final var alphaIdentifier = new GraphNodeIdentifier(graph, "Alpha");
		final var classNodeAlpha =
				new GraphNodeClass(
						graph,
						alphaIdentifier
				);
		packageNode.has(classNodeAlpha);
		final var fooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var operationNodeAlphaFoo = new GraphNodeOperation(graph, fooIdentifier);
		classNodeAlpha.has(operationNodeAlphaFoo);
		
		final var betaIdentifier = new GraphNodeIdentifier(graph, "Beta");
		final var classNodeBeta =
				new GraphNodeClass(
						graph,
						betaIdentifier
				);
		packageNode.has(classNodeBeta);
		classNodeBeta.is(classNodeAlpha);
		final var operationNodeBetaFoo = new GraphNodeOperation(graph, fooIdentifier);
		classNodeBeta.has(operationNodeBetaFoo);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		addMethodNode.adds(operationNodeBetaFoo);
		startNode.initiates(addMethodNode);
		final var forcedOverrideRisk = new GraphNodeRiskForcedOverride(graph);
		forcedOverrideRisk.affects(operationNodeAlphaFoo);
		forcedOverrideRisk.affects(operationNodeBetaFoo);
		addMethodNode.causes(forcedOverrideRisk);
		
		return graph;
	}
	
	private static Graph constructImposedSpecificationGraph()
			throws RefactoringMayContainOnlyOneStartNodeException {
		final var graph = new Graph("AM-3 Imposed Specification");
		
		// Code
		final var packageNode = GraphNodePackage.parse(graph, "nl.ou.refactoring.dangers.imposedSpecification");

		// Code: Alpha
		final var alphaIdentifier = new GraphNodeIdentifier(graph, "Alpha");
		final var classNodeAlpha =
			new GraphNodeClass(
				graph,
				alphaIdentifier
			);
		packageNode.has(classNodeAlpha);
		final var fooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var operationNodeAlphaFoo = new GraphNodeOperation(graph, fooIdentifier);
		classNodeAlpha.has(operationNodeAlphaFoo);
		// Code: Beta
		final var betaIdentifier = new GraphNodeIdentifier(graph, "Beta");
		final var classNodeBeta =
			new GraphNodeClass(
				graph,
				betaIdentifier
			);
		packageNode.has(classNodeBeta);
		classNodeBeta.is(classNodeAlpha);
		final var operationNodeBetaFoo = new GraphNodeOperation(graph, fooIdentifier);
		classNodeBeta.has(operationNodeBetaFoo);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		addMethodNode.adds(operationNodeAlphaFoo);
		startNode.initiates(addMethodNode);
		final var imposedSpecificationRisk = new GraphNodeRiskImposedSpecification(graph);
		imposedSpecificationRisk.affects(operationNodeAlphaFoo);
		imposedSpecificationRisk.affects(operationNodeBetaFoo);
		addMethodNode.causes(imposedSpecificationRisk);
		
		return graph;
	}
	
	private static Graph constructPrecedingOverloadGraph()
			throws RefactoringMayContainOnlyOneStartNodeException {
		final var graph = new Graph("AM-4 Preceding Overload");
		
		// Code
		final var intType = new GraphNodeType(graph, "int");
		final var doubleType = new GraphNodeType(graph, "double");
		final var packageNode = GraphNodePackage.parse(graph, "nl.ou.refactoring.dangers.precedingOverload");
		final var alphaIdentifier = new GraphNodeIdentifier(graph, "Alpha");
		final var alphaClassNode =
			new GraphNodeClass(
				graph,
				alphaIdentifier
			);
		packageNode.has(alphaClassNode);
		final var operationNodeFooIntParameters = new ArrayList<GraphNodeOperationParameter>();
		final var operationNodeFooIntParameter = new GraphNodeOperationParameter(graph, "number");
		operationNodeFooIntParameter.is(intType);
		operationNodeFooIntParameters.add(operationNodeFooIntParameter);
		final var fooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var operationNodeFooInt =
			new GraphNodeOperation(
				graph,
				fooIdentifier,
				operationNodeFooIntParameters
			);
		alphaClassNode.has(operationNodeFooInt);
		final var operationNodeFooDoubleParameters = new ArrayList<GraphNodeOperationParameter>();
		final var operationNodeFooDoubleParameter = new GraphNodeOperationParameter(graph, "number");
		operationNodeFooDoubleParameter.is(doubleType);
		operationNodeFooDoubleParameters.add(operationNodeFooDoubleParameter);
		final var operationNodeFooDouble =
			new GraphNodeOperation(
				graph,
				fooIdentifier,
				operationNodeFooDoubleParameters
			);
		alphaClassNode.has(operationNodeFooDouble);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		startNode.initiates(addMethodNode);
		addMethodNode.adds(operationNodeFooDouble);
		final var precedingOverloadRisk = new GraphNodeRiskPrecedingOverload(graph);
		precedingOverloadRisk.affects(operationNodeFooInt);
		precedingOverloadRisk.affects(operationNodeFooDouble);
		addMethodNode.causes(precedingOverloadRisk);
		
		return graph;
	}
	
	private static Graph constructChangedNestedRelationship()
			throws RefactoringMayContainOnlyOneStartNodeException {
		final var graph = new Graph("AM-5 Changed Nested Relationship");
		
		// Code
		final var packageNode = GraphNodePackage.parse(graph, "nl.ou.refactoring.dangers.changedNestedRelationship");
		final var alphaIdentifierNode = new GraphNodeIdentifier(graph, "Alpha");
		final var alphaClassNode =
			new GraphNodeClass(
				graph,
				alphaIdentifierNode
			);
		packageNode.has(alphaClassNode);
		final var fooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var alphaFooOperationNode =
			new GraphNodeOperation(
				graph,
				fooIdentifier
			);
		alphaClassNode.has(alphaFooOperationNode);
		final var betaIdentifierNode = new GraphNodeIdentifier(graph, "Beta");
		final var betaClassNode =
			new GraphNodeClass(
				graph,
				betaIdentifierNode
			);
		alphaClassNode.has(betaClassNode);
		final var barIdentifier = new GraphNodeIdentifier(graph, "bar");
		final var betaBarOperationNode =
			new GraphNodeOperation(
				graph,
				barIdentifier
			);
		betaClassNode.has(betaBarOperationNode);
		final var betaBarBlockNode = new GraphNodeBlock(graph);
		betaBarOperationNode.hasBody(betaBarBlockNode);
		final var betaBarMethodInvocationExpressionNode = new GraphNodeMethodInvocationExpression(graph);
		betaBarMethodInvocationExpressionNode.invokes(alphaFooOperationNode);
		final var betaFooOperationNode =
			new GraphNodeOperation(
				graph,
				fooIdentifier
			);
		betaClassNode.has(betaFooOperationNode);
		final var betaBarExpressionStatementNode = new GraphNodeExpressionStatement(graph, betaBarMethodInvocationExpressionNode);
		betaBarBlockNode.has(betaBarExpressionStatementNode);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		startNode.initiates(addMethodNode);
		addMethodNode.adds(betaFooOperationNode);
		addMethodNode.finalises();
		
		final var changedNestedRelationshipRiskNode = new GraphNodeRiskChangedNestedRelationship(graph);
		changedNestedRelationshipRiskNode.affects(alphaFooOperationNode);
		changedNestedRelationshipRiskNode.affects(betaFooOperationNode);
		changedNestedRelationshipRiskNode.affects(betaBarMethodInvocationExpressionNode);
		addMethodNode.causes(changedNestedRelationshipRiskNode);
		
		return graph;
	}
}