package nl.ou.refactoring.advice.io.javaParser;

import java.io.Reader;
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
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
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
		
		// Package
		final var packageDeclaration = compilationUnit.getPackageDeclaration();
		final var packageNameString =
			packageDeclaration.isPresent()
				? packageDeclaration.get().getNameAsString()
				: "default";
		final var packageNode = GraphNodePackage.parse(graph, packageNameString);
		
		// Class
		final var classes =
			compilationUnit
				.getTypes()
				.stream()
				.filter(typeDeclaration -> typeDeclaration.isClassOrInterfaceDeclaration())
				.collect(Collectors.toUnmodifiableList());				
		
		return this.graph;
	}
}