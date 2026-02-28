package nl.ou.refactoring.advice.integrationTests;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphTemplates;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeBlock;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeExpressionStatement;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

public final class RefactoringTestsArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) throws RefactoringMayContainOnlyOneStartNodeException, ArgumentNullException, GraphReaderException {
		return
			Stream.of(
				moveFieldFromClassToClassReferenceWithinSourceClass(),
				moveMethodFromClassToClassInvocationInSourceClass(),
				moveMethodFromClassToClassInvocationOutsideClass()
			)
			.map(Arguments::of);
	}
	
	private static Graph moveFieldFromClassToClassReferenceWithinSourceClass() throws GraphReaderException {
		/* Load template. */
		final var graph = GraphTemplates.moveField().clone("Move Field from Class to Class with reference from within the source Class");
		final var addFieldMicrostepNode =
				graph
					.getNodes(GraphNodeMicrostepAddField.class)
					.stream()
					.findAny()
					.get();
			final var removeFieldMicrostepNode =
				graph
					.getNodes(GraphNodeMicrostepRemoveField.class)
					.stream()
					.findAny()
					.get();
		
		/* Introduce code nodes and edges. */
		final var packageNode = new GraphNodePackage(graph, "nl.ou.refactoring.moveField.classToClass");
		
		final var alphaClassNode = new GraphNodeClass(graph, "Alpha");
		final var alphaOneAttributeNode = new GraphNodeAttribute(graph, "one");
		final var alphaTwoAttributeNode = new GraphNodeAttribute(graph, "two");
		final var alphaBarOperationNode = new GraphNodeOperation(graph, "bar");
		final var alphaFooOperationNode = new GraphNodeOperation(graph, "foo");
		packageNode.has(alphaClassNode);
		alphaClassNode.has(alphaOneAttributeNode);
		alphaClassNode.has(alphaTwoAttributeNode);
		alphaClassNode.has(alphaBarOperationNode);
		alphaClassNode.has(alphaFooOperationNode);
		removeFieldMicrostepNode.removes(alphaTwoAttributeNode);
		
		final var betaClassNode = new GraphNodeClass(graph, "Beta");
		final var betaField1AttributeNode = new GraphNodeAttribute(graph, "field1");
		final var betaField2AttributeNode = new GraphNodeAttribute(graph, "field2");
		final var betaTwoAttributeNode = new GraphNodeAttribute(graph, "two");
		final var betaFooOperationNode = new GraphNodeOperation(graph, "foo");
		packageNode.has(betaClassNode);
		betaClassNode.has(betaField1AttributeNode);
		betaClassNode.has(betaField2AttributeNode);
		betaClassNode.has(betaTwoAttributeNode);
		betaClassNode.has(betaFooOperationNode);
		addFieldMicrostepNode.adds(betaTwoAttributeNode);
		
		final var missingDefinitionRiskNode = new GraphNodeRiskMissingDefinition(graph);
		removeFieldMicrostepNode.causes(missingDefinitionRiskNode);
		missingDefinitionRiskNode.affects(alphaBarOperationNode);
		missingDefinitionRiskNode.affects(alphaTwoAttributeNode);
		
		return graph;
	}
	
	private static Graph moveMethodFromClassToClassInvocationInSourceClass() throws GraphReaderException {
		/* Load template. */
		final var graph = GraphTemplates.moveMethod().clone("Move Method from Class to Class with source invocation");
		final var addMethodMicrostepNode =
			graph
				.getNodes(GraphNodeMicrostepAddMethod.class)
				.stream()
				.findAny()
				.get();
		final var removeMethodMicrostepNode =
			graph
				.getNodes(GraphNodeMicrostepRemoveMethod.class)
				.stream()
				.findAny()
				.get();
		
		/* Introduce code nodes and edges. */
		final var packageNode = new GraphNodePackage(graph, "nl.ou.refactoring.moveMethod.classToClass");
		
		final var alphaClassNode = new GraphNodeClass(graph, "Alpha");
		final var alphaOneAttributeNode = new GraphNodeAttribute(graph, "one");
		final var alphaTwoAttributeNode = new GraphNodeAttribute(graph, "two");
		final var alphaBarOperationNode = new GraphNodeOperation(graph, "bar");
		final var alphaFooOperationNode = new GraphNodeOperation(graph, "foo");
		packageNode.has(alphaClassNode);
		alphaClassNode.has(alphaOneAttributeNode);
		alphaClassNode.has(alphaTwoAttributeNode);
		alphaClassNode.has(alphaBarOperationNode);
		alphaClassNode.has(alphaFooOperationNode);
		removeMethodMicrostepNode.removes(alphaFooOperationNode);
		final var alphaBarBlock = new GraphNodeBlock(graph);
		final var alphaBarExpression = new GraphNodeMethodInvocationExpression(graph);
		final var alphaBarStatement = new GraphNodeExpressionStatement(graph, alphaBarExpression);
		alphaBarOperationNode.hasBody(alphaBarBlock);
		alphaBarBlock.has(alphaBarStatement);
		alphaBarExpression.invokes(alphaFooOperationNode);
		
		final var betaClassNode = new GraphNodeClass(graph, "Beta");
		final var betaField1AttributeNode = new GraphNodeAttribute(graph, "field1");
		final var betaField2AttributeNode = new GraphNodeAttribute(graph, "field2");
		final var betaFooOperationNode = new GraphNodeOperation(graph, "foo");
		packageNode.has(betaClassNode);
		betaClassNode.has(betaField1AttributeNode);
		betaClassNode.has(betaField2AttributeNode);
		betaClassNode.has(betaFooOperationNode);
		addMethodMicrostepNode.adds(betaFooOperationNode);
		
		final var missingDefinitionRiskNode = new GraphNodeRiskMissingDefinition(graph);
		removeMethodMicrostepNode.causes(missingDefinitionRiskNode);
		missingDefinitionRiskNode.affects(alphaBarOperationNode);
		missingDefinitionRiskNode.affects(alphaFooOperationNode);
		
		return graph;
	}
	
	private static Graph moveMethodFromClassToClassInvocationOutsideClass() throws GraphReaderException {
		/* Load template. */
		final var graph = GraphTemplates.moveMethod().clone("Move Method from Class to Class with exterior invocation");
		final var addMethodMicrostepNode =
			graph
				.getNodes(GraphNodeMicrostepAddMethod.class)
				.stream()
				.findAny()
				.get();
		final var removeMethodMicrostepNode =
			graph
				.getNodes(GraphNodeMicrostepRemoveMethod.class)
				.stream()
				.findAny()
				.get();
		
		/* Introduce code nodes and edges. */
		final var packageNode = new GraphNodePackage(graph, "nl.ou.refactoring.moveMethod.classToClass");
		
		final var alphaClassNode = new GraphNodeClass(graph, "Alpha");
		final var alphaOneAttributeNode = new GraphNodeAttribute(graph, "one");
		final var alphaTwoAttributeNode = new GraphNodeAttribute(graph, "two");
		final var alphaBarOperationNode = new GraphNodeOperation(graph, "bar");
		final var alphaFooOperationNode = new GraphNodeOperation(graph, "foo");
		packageNode.has(alphaClassNode);
		alphaClassNode.has(alphaOneAttributeNode);
		alphaClassNode.has(alphaTwoAttributeNode);
		alphaClassNode.has(alphaBarOperationNode);
		alphaClassNode.has(alphaFooOperationNode);
		removeMethodMicrostepNode.removes(alphaFooOperationNode);
		
		final var betaClassNode = new GraphNodeClass(graph, "Beta");
		final var betaField1AttributeNode = new GraphNodeAttribute(graph, "field1");
		final var betaField2AttributeNode = new GraphNodeAttribute(graph, "field2");
		final var betaFooOperationNode = new GraphNodeOperation(graph, "foo");
		packageNode.has(betaClassNode);
		betaClassNode.has(betaField1AttributeNode);
		betaClassNode.has(betaField2AttributeNode);
		betaClassNode.has(betaFooOperationNode);
		addMethodMicrostepNode.adds(betaFooOperationNode);
		
		final var gammaClassNode = new GraphNodeClass(graph, "Gamma");
		final var gammaCallerOperationNode = new GraphNodeOperation(graph, "caller");
		final var gammaCallerBlock = new GraphNodeBlock(graph);
		final var gammaCallerExpression = new GraphNodeMethodInvocationExpression(graph);
		final var gammaCallerStatement = new GraphNodeExpressionStatement(graph, gammaCallerExpression);
		packageNode.has(gammaClassNode);
		gammaClassNode.has(gammaCallerOperationNode);
		gammaCallerOperationNode.hasBody(gammaCallerBlock);
		gammaCallerBlock.has(gammaCallerStatement);
		gammaCallerExpression.invokes(alphaFooOperationNode);
		
		final var missingDefinitionRiskNode = new GraphNodeRiskMissingDefinition(graph);
		removeMethodMicrostepNode.causes(missingDefinitionRiskNode);
		missingDefinitionRiskNode.affects(gammaCallerOperationNode);
		missingDefinitionRiskNode.affects(alphaFooOperationNode);
		
		return graph;
	}
}
