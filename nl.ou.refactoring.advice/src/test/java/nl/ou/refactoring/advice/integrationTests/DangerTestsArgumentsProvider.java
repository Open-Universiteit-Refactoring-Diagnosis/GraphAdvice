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
						"Alpha",
						GraphNodeClassStereotype.BEFORE
				);
		packageNode.has(alphaClassNode);
		final var fooOperationNode = new GraphNodeOperation(graph, "foo");
		alphaClassNode.has(fooOperationNode);
		final var alphaNewClassNode =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.AFTER
				);
		packageNode.has(alphaNewClassNode);
		final var fooNewOperationNode = new GraphNodeOperation(graph, "foo");
		alphaNewClassNode.has(fooNewOperationNode);
		final var fooNewDoubleOperationNode = new GraphNodeOperation(graph, "foo");
		alphaNewClassNode.has(fooNewDoubleOperationNode);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		addMethodNode.adds(fooNewDoubleOperationNode);
		startNode.initiates(addMethodNode);
		final var doubleDefinitionRisk = new GraphNodeRiskDoubleDefinition(graph);
		doubleDefinitionRisk.affects(fooOperationNode);
		doubleDefinitionRisk.affects(fooNewDoubleOperationNode);
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
		
		// BEFORE
		final var alphaClassNodeBefore =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.BEFORE
				);
		packageNode.has(alphaClassNodeBefore);
		final var fooOperationNodeBefore = new GraphNodeOperation(graph, "foo");
		alphaClassNodeBefore.has(fooOperationNodeBefore);
		
		final var betaClassNodeBefore =
				new GraphNodeClass(
						graph,
						"Beta",
						GraphNodeClassStereotype.BEFORE
				);
		packageNode.has(betaClassNodeBefore);
		betaClassNodeBefore.is(alphaClassNodeBefore);
		
		// AFTER
		final var alphaClassNodeAfter =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.AFTER
				);
		packageNode.has(alphaClassNodeAfter);
		final var fooOperationNodeAfter = new GraphNodeOperation(graph, "foo");
		alphaClassNodeAfter.has(fooOperationNodeAfter);
		
		final var betaClassNodeAfter =
				new GraphNodeClass(
						graph,
						"Beta",
						GraphNodeClassStereotype.AFTER
				);
		packageNode.has(betaClassNodeAfter);
		betaClassNodeAfter.is(alphaClassNodeAfter);
		final var fooOperationNodeAfterNew = new GraphNodeOperation(graph, "foo");
		betaClassNodeAfter.has(fooOperationNodeAfterNew);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		addMethodNode.adds(fooOperationNodeAfterNew);
		startNode.initiates(addMethodNode);
		final var forcedOverrideRisk = new GraphNodeRiskForcedOverride(graph);
		forcedOverrideRisk.affects(fooOperationNodeBefore);
		forcedOverrideRisk.affects(fooOperationNodeAfterNew);
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
		// BEFORE
		// BEFORE: Alpha
		final var alphaClassNodeBefore =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.BEFORE
				);
		packageNode.has(alphaClassNodeBefore);
		// BEFORE: Beta
		final var betaClassNodeBefore =
				new GraphNodeClass(
						graph,
						"Beta",
						GraphNodeClassStereotype.BEFORE
				);
		packageNode.has(betaClassNodeBefore);
		betaClassNodeBefore.is(alphaClassNodeBefore);
		final var fooOperationNodeBetaBefore = new GraphNodeOperation(graph, "foo");
		betaClassNodeBefore.has(fooOperationNodeBetaBefore);
		
		// AFTER
		// AFTER: Alpha
		final var alphaClassNodeAfter =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.AFTER
				);
		packageNode.has(alphaClassNodeAfter);
		final var fooOperationNodeAlphaAfterNew = new GraphNodeOperation(graph, "foo");
		alphaClassNodeAfter.has(fooOperationNodeAlphaAfterNew);
		// AFTER: Beta
		final var betaClassNodeAfter =
				new GraphNodeClass(
						graph,
						"Beta",
						GraphNodeClassStereotype.AFTER
				);
		packageNode.has(betaClassNodeAfter);
		betaClassNodeAfter.is(alphaClassNodeAfter);
		final var fooOperationNodeBetaAfter = new GraphNodeOperation(graph, "foo");
		betaClassNodeAfter.has(fooOperationNodeBetaAfter);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		addMethodNode.adds(fooOperationNodeAlphaAfterNew);
		startNode.initiates(addMethodNode);
		final var imposedSpecificationRisk = new GraphNodeRiskImposedSpecification(graph);
		imposedSpecificationRisk.affects(fooOperationNodeAlphaAfterNew);
		imposedSpecificationRisk.affects(fooOperationNodeBetaAfter);
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
		final var alphaClassNodeBefore =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.BEFORE
				);
		packageNode.has(alphaClassNodeBefore);
		final var fooOperationNodeBeforeIntParameters = new ArrayList<GraphNodeOperationParameter>();
		final var fooOperationNodeBeforeIntParameter = new GraphNodeOperationParameter(graph, "number");
		fooOperationNodeBeforeIntParameter.is(intType);
		fooOperationNodeBeforeIntParameters.add(fooOperationNodeBeforeIntParameter);
		final var fooOperationNodeBefore =
				new GraphNodeOperation(
						graph,
						"foo",
						fooOperationNodeBeforeIntParameters
				);
		alphaClassNodeBefore.has(fooOperationNodeBefore);
		final var alphaClassNodeAfter =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.AFTER
				);
		packageNode.has(alphaClassNodeAfter);
		final var fooOperationNodeAfterIntParameters = new ArrayList<GraphNodeOperationParameter>();
		final var fooOperationNodeAfterIntParameter = new GraphNodeOperationParameter(graph, "number");
		fooOperationNodeAfterIntParameter.is(intType);
		fooOperationNodeAfterIntParameters.add(fooOperationNodeAfterIntParameter);
		final var fooOperationNodeAfter =
				new GraphNodeOperation(
						graph,
						"foo",
						fooOperationNodeAfterIntParameters
				);
		alphaClassNodeAfter.has(fooOperationNodeAfter);
		final var fooOperationNodeAfterDoubleParameters = new ArrayList<GraphNodeOperationParameter>();
		final var fooOperationNodeAfterDoubleParameter = new GraphNodeOperationParameter(graph, "number");
		fooOperationNodeAfterDoubleParameter.is(doubleType);
		fooOperationNodeAfterDoubleParameters.add(fooOperationNodeAfterDoubleParameter);
		final var fooOperationNodeAfterNew =
				new GraphNodeOperation(
						graph,
						"foo",
						fooOperationNodeAfterDoubleParameters
				);
		alphaClassNodeAfter.has(fooOperationNodeAfterNew);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		startNode.initiates(addMethodNode);
		final var precedingOverloadRisk = new GraphNodeRiskPrecedingOverload(graph);
		precedingOverloadRisk.affects(fooOperationNodeBefore);
		precedingOverloadRisk.affects(fooOperationNodeAfterNew);
		addMethodNode.causes(precedingOverloadRisk);
		
		return graph;
	}
}