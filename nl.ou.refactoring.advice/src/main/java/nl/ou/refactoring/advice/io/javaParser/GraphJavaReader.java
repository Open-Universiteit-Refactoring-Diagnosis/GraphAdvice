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
import nl.ou.refactoring.advice.nodes.code.GraphNodeProgramLocation;
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
	 * The Refactoring Advice Graph that will contain the code nodes.
	 */
	private Graph graph;
	
	/**
	 * Reads the Java source code.
	 */
	private final Reader reader;
	
	/**
	 * The full file name of the Java source code, including its path.
	 */
	private final String fileNameFull;
	
	/**
	 * The short file name of the Java source code.
	 */
	private final String fileName;

	/**
	 * Initialises a new instance of {@link GraphJavaReader}.
	 * @param refactoringName The name of the refactoring.
	 * @param reader Reads the Java source code.
	 * @param fileNameFull The full file name of the Java source code, including its path.
	 * @param fileName The short file name of the Java source code.
	 * @throws ArgumentNullException Thrown if refactoringName or reader is null.
	 * @throws ArgumentEmptyException Thrown if refactoringName, fileNameFull or fileName is empty or contains only white spaces.
	 */
	public GraphJavaReader(String refactoringName, Reader reader, String fileNameFull, String fileName)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(refactoringName, "refactoringName");
		ArgumentGuard.requireNotNull(reader, "reader");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(fileNameFull, "fileNameFull");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(fileName, "fileName");
		this.graph = new Graph(refactoringName);
		this.reader = reader;
		this.fileNameFull = fileNameFull;
		this.fileName = fileName;
	}
	
	/**
	 * Initialises a new instance of {@link GraphJavaReader}.
	 * @param graph The graph to which to add the code nodes from the Java source code.
	 * @param reader Reads the Java source code.
	 * @param fileNameFull The full file name of the Java source code, including its path.s
	 * @param fileName The short file name of the Java source code.
	 * @throws ArgumentNullException Thrown if graph or reader is null.
	 * @throws ArgumentEmptyException Thrown if fileNameFull or fileName is empty or contains only white spaces.
	 */
	public GraphJavaReader(Graph graph, Reader reader, String fileNameFull, String fileName)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(reader, "reader");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(fileNameFull, "fileNameFull");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(fileName, "fileName");
		this.graph = graph;
		this.reader = reader;
		this.fileNameFull = fileNameFull;
		this.fileName = fileName;
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
		GraphNodePackage.parse(graph, packageNameString);
		final var packageNode =
			graph
				.getNode(packageNameString, GraphNodePackage.class)
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
				packageNode.has(classNode);
				final var classPositionBegin = classOrInterfaceDeclaration.getBegin().get();
				final var classPositionEnd = classOrInterfaceDeclaration.getEnd().get();
				final var classProgramLocationNode =
					new GraphNodeProgramLocation(
						graph,
						this.fileNameFull,
						this.fileName,
						classPositionBegin.line,
						classPositionBegin.column,
						classPositionEnd.line,
						classPositionEnd.column
					);
				classNode.has(classProgramLocationNode);
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
							final var attributePositionBegin = variableDeclaration.getBegin().get();
							final var attributePositionEnd = variableDeclaration.getEnd().get();
							final var attributeProgramLocationNode =
								new GraphNodeProgramLocation(
									graph,
									this.fileNameFull,
									this.fileName,
									attributePositionBegin.line,
									attributePositionBegin.column,
									attributePositionEnd.line,
									attributePositionEnd.column
								);
							attributeNode.has(attributeProgramLocationNode);
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
						final var operationPositionBegin = methodDeclaration.getBegin().get();
						final var operationPositionEnd = methodDeclaration.getEnd().get();
						final var operationProgramLocationNode =
							new GraphNodeProgramLocation(
								graph,
								this.fileNameFull,
								this.fileName,
								operationPositionBegin.line,
								operationPositionBegin.column,
								operationPositionEnd.line,
								operationPositionEnd.column
							);
						operationNode.has(operationProgramLocationNode);
						classNode.has(operationNode);
					}
				}
			}
		}
		
		return this.graph;
	}
}