package nl.ou.refactoring.advice.integrationTests;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClassStereotype;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskForcedOverride;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskImposedSpecification;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskPrecedingOverload;

public final class DangerTestsArgumentsProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(
			ParameterDeclarations parameters,
			ExtensionContext context)
					throws RefactoringMayContainOnlyOneStartNodeException {
		return
				Stream.of(
						constructDoubleDefinitionGraph(),
						constructForcedOverrideGraph(),
						constructImposedSpecificationGraph(),
						constructPrecedingOverloadGraph()
				)
				.map(Arguments::of);
	}
	
	private static Graph constructDoubleDefinitionGraph()
			throws RefactoringMayContainOnlyOneStartNodeException {
		final var graph = new Graph("AM-1 Double Definition");
		
		// Code
		final var packageNode =
				new GraphNodePackage(
						graph,
						"nl.ou.refactoring.doubleDefinition"
				);
		final var alphaClassNode =
				new GraphNodeClass(
						graph,
						"Alpha"
				);
		packageNode.has(alphaClassNode);
		final var fooOperationNode = new GraphNodeOperation(graph, "foo");
		alphaClassNode.has(fooOperationNode);

		final var fooNewOperationNode = new GraphNodeOperation(graph, "foo");
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
		final var packageNode =
				new GraphNodePackage(
						graph,
						"nl.ou.refactoring.dangers.forcedOverride"
				);
		
		final var classNodeAlpha =
				new GraphNodeClass(
						graph,
						"Alpha"
				);
		packageNode.has(classNodeAlpha);
		final var operationNodeAlphaFoo = new GraphNodeOperation(graph, "foo");
		classNodeAlpha.has(operationNodeAlphaFoo);
		
		final var classNodeBeta =
				new GraphNodeClass(
						graph,
						"Beta"
				);
		packageNode.has(classNodeBeta);
		classNodeBeta.is(classNodeAlpha);
		final var operationNodeBetaFoo = new GraphNodeOperation(graph, "foo");
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
		final var packageNode =
				new GraphNodePackage(
						graph,
						"nl.ou.refactoring.dangers.imposedSpecification"
				);
		// Code: Alpha
		final var classNodeAlpha =
				new GraphNodeClass(
						graph,
						"Alpha"
				);
		packageNode.has(classNodeAlpha);
		final var operationNodeAlphaFoo = new GraphNodeOperation(graph, "foo");
		classNodeAlpha.has(operationNodeAlphaFoo);
		// Code: Beta
		final var classNodeBeta =
				new GraphNodeClass(
						graph,
						"Beta"
				);
		packageNode.has(classNodeBeta);
		classNodeBeta.is(classNodeAlpha);
		final var operationNodeBetaFoo = new GraphNodeOperation(graph, "foo");
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
		final var packageNode =
				new GraphNodePackage(graph, "nl.ou.refactoring.dangers.precedingOverload");
		final var alphaClassNode =
				new GraphNodeClass(
						graph,
						"Alpha"
				);
		packageNode.has(alphaClassNode);
		final var operationNodeFooIntParameters = new ArrayList<GraphNodeOperationParameter>();
		final var operationNodeFooIntParameter = new GraphNodeOperationParameter(graph, "number");
		operationNodeFooIntParameter.is(intType);
		operationNodeFooIntParameters.add(operationNodeFooIntParameter);
		final var operationNodeFooInt =
				new GraphNodeOperation(
						graph,
						"foo",
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
						"foo",
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
}