package nl.ou.refactoring.advice.io.javaParser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphReader;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

/**
 * Reads code nodes for a Refactoring Advice Graph.
 */
public final class GraphJavaReader implements GraphReader {
	/**
	 * Reads the Java source code.
	 */
	private final Reader reader;
	
	/**
	 * The Refactoring Advice Graph that will contain the code nodes.
	 */
	private Graph graph;

	/**
	 * Initialises a new instance of {@link GraphJavaReader}.
	 * @param refactoringName The name of the refactoring.
	 * @param reader Reads the Java source code.
	 * @throws ArgumentNullException Thrown if refactoringName or reader is null.
	 * @throws ArgumentEmptyException Thrown if refactoringName is empty or contains only white spaces.
	 */
	public GraphJavaReader(String refactoringName, Reader reader)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(refactoringName, "refactoringName");
		ArgumentGuard.requireNotNull(reader, "reader");
		this.graph = new Graph(refactoringName);
		this.reader = reader;
	}
	
	/**
	 * Initialises a new instance of {@link GraphJavaReader}.
	 * @param graph The graph to which to add the code nodes from the Java source code.
	 * @param reader Reads the Java source code.
	 * @throws ArgumentNullException Thrown if graph or reader is null.
	 */
	public GraphJavaReader(Graph graph, Reader reader)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(reader, "reader");
		this.graph = graph;
		this.reader = reader;
	}

	@Override
	public Graph read()
			throws GraphReaderException {
		CompilationUnit compilationUnit;
		try {
			compilationUnit = StaticJavaParser.parse(this.reader);
		} catch (ParseProblemException exception) {
			throw new GraphJavaReaderParseFailedException(exception);
		}
		
		final var typeMap = new HashMap<String, GraphNodeType>();
		
		// Package
		final var packageDeclaration = compilationUnit.getPackageDeclaration();
		final var packageNameString =
			packageDeclaration.isPresent()
				? packageDeclaration.get().getNameAsString()
				: "default";
		final var packageNodeRoot = GraphNodePackage.parse(graph, packageNameString);
		final var packageNodeLeaf =
			packageNodeRoot
				.getPackageNodeLeafs()
				.stream()
				.findFirst()
				.get();
		
		// Classes and interfaces
		final var classesAndInterfaces =
			compilationUnit
				.getTypes()
				.stream()
				.filter(typeDeclaration -> typeDeclaration.isClassOrInterfaceDeclaration())
				.map(typeDeclaration -> typeDeclaration.asClassOrInterfaceDeclaration())
				.collect(Collectors.toUnmodifiableList());		
		for (final var classOrInterfaceDeclaration : classesAndInterfaces) {
			if (classOrInterfaceDeclaration.isInterface()) {
				// TODO implement
			} else {
				final var classNodeIdentifier = new GraphNodeIdentifier(graph, classOrInterfaceDeclaration.getNameAsString());
				final var classNode = new GraphNodeClass(graph, classNodeIdentifier);
				packageNodeLeaf.has(classNode);
				for (final var member : classOrInterfaceDeclaration.getMembers()) {
					if (member.isFieldDeclaration()) {
						final var fieldDeclaration = member.asFieldDeclaration();
						for (final var variableDeclaration : fieldDeclaration.getVariables()) {
							final var attributeNode = new GraphNodeAttribute(graph, variableDeclaration.getNameAsString());
							final var attributeTypeName = variableDeclaration.getTypeAsString();
							final var attributeType =
								typeMap
									.computeIfAbsent(
										attributeTypeName,
										_ -> new GraphNodeType(graph, attributeTypeName)
									);
							attributeNode.is(attributeType);
							classNode.has(attributeNode);
						}
					}
					if (member.isMethodDeclaration()) {
						final var methodDeclaration = member.asMethodDeclaration();
						final var operationIdentifier = new GraphNodeIdentifier(graph, methodDeclaration.getNameAsString());
						final var operationReturnTypeName = methodDeclaration.getTypeAsString();
						final var operationReturnType =
							typeMap
								.computeIfAbsent(
									operationReturnTypeName,
									_ -> new GraphNodeType(graph, operationReturnTypeName)
								);

						
						final var operationParameters = new ArrayList<GraphNodeOperationParameter>();
						for (final var methodParameter : methodDeclaration.getParameters()) {
							final var operationParameterName = methodParameter.getNameAsString();
							final var operationParameterTypeName = methodParameter.getTypeAsString();
							final var operationParameterType =
								typeMap
									.computeIfAbsent(
											operationParameterTypeName,
										_ -> new GraphNodeType(graph, operationParameterTypeName)
									);
							final var operationParameter = new GraphNodeOperationParameter(graph, operationParameterName);
							operationParameter.is(operationParameterType);
							operationParameters.add(operationParameter);
						}
						
						final var operationNode = new GraphNodeOperation(graph, operationIdentifier, operationParameters);
						operationNode.hasReturnType(operationReturnType);
						classNode.has(operationNode);
					}
				}
			}
		}
		
		return this.graph;
	}
}