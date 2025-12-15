package nl.ou.refactoring.advice.integrationTests;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClassStereotype;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskForcedOverride;

public final class DangerTestsArgumentsProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(
			ParameterDeclarations parameters,
			ExtensionContext context)
					throws RefactoringMayContainOnlyOneStartNodeException {
		return
				Stream.of(
						constructDoubleDefinitionGraph(),
						constructForcedOverrideGraph()
				)
				.map(Arguments::of);
	}
	
	private static Graph constructDoubleDefinitionGraph()
			throws RefactoringMayContainOnlyOneStartNodeException {
		final var graph = new Graph("AM-1 Double Definition");
		
		// Code
		final var packageNode = new GraphNodePackage(graph, "nl.ou.refactoring.doubleDefinition");
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
		final var packageNode = new GraphNodePackage(graph, "nl.ou.refactoring.dangers.forcedOverride");
		final var alphaClassNode = new GraphNodeClass(graph, "Alpha", null);
		packageNode.has(alphaClassNode);
		final var fooOperationNode = new GraphNodeOperation(graph, "foo");
		alphaClassNode.has(fooOperationNode);
		
		final var betaClassNodeBefore =
				new GraphNodeClass(
						graph,
						"Beta",
						GraphNodeClassStereotype.BEFORE
				);
		packageNode.has(betaClassNodeBefore);
		betaClassNodeBefore.is(alphaClassNode);
		final var betaClassNodeAfter =
				new GraphNodeClass(
						graph,
						"Beta",
						GraphNodeClassStereotype.AFTER
				);
		packageNode.has(betaClassNodeAfter);
		betaClassNodeAfter.is(alphaClassNode);
		final var fooNewOperationNode = new GraphNodeOperation(graph, "foo");
		betaClassNodeAfter.has(fooNewOperationNode);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		startNode.initiates(addMethodNode);
		final var forcedOverrideRisk = new GraphNodeRiskForcedOverride(graph);
		forcedOverrideRisk.affects(fooOperationNode);
		forcedOverrideRisk.affects(fooNewOperationNode);
		addMethodNode.causes(forcedOverrideRisk);
		
		return graph;
	}

}
