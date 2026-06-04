package nl.ou.refactoring.advice.eclipse;

import java.io.FileNotFoundException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReader;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReaderResolutionProvider;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

/**
 * Resolves Refactoring Advice Graph (RAG) nodes that may not have been loaded
 * in the Graph yet, using the Eclipse IDE as its context.
 */
public final class EclipseResolutionProvider implements GraphJavaReaderResolutionProvider {
	/**
	 * Logs messages for debugging and troubleshooting.
	 */
	private static final Logger LOGGER = LogManager.getLogger(EclipseResolutionProvider.class);

	/**
	 * The Java Project from which a symbol should be resolved.
	 */
	private final IJavaProject javaProject;

	/**
	 * Initialises a new instance of {@link EclipseResolutionProvider}.
	 * 
	 * @throws ArgumentNullException Thrown if javaProject is null.
	 */
	public EclipseResolutionProvider(IJavaProject javaProject) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(javaProject, "javaProject");
		this.javaProject = javaProject;
	}

	@Override
	public <T extends GraphNodeCode> Optional<T> resolveByFullyQualifiedName(
			Graph graph,
			String fullyQualifiedName,
			Class<T> resultType) {

		if (resultType == GraphNodeClass.class) {
			LOGGER.info("Attempting to resolve Class '{}' from project", fullyQualifiedName);
			try {
				final var type = this.javaProject.findType(fullyQualifiedName);
				graph = readElementToGraph(graph, type);
				return graph.getNode(fullyQualifiedName, resultType);
			} catch (JavaModelException ex) {
				ex.printStackTrace();
				LOGGER.error("Resolution of Class '" + fullyQualifiedName + "' failed", ex);
				return Optional.empty();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				LOGGER.error("Failed to read source file for Class '" + fullyQualifiedName + "'", ex);
				return Optional.empty();
			}
		}

		LOGGER.warn("Unsupported node type '{}'", resultType.getName());
		return Optional.empty();
	}

	private Graph readElementToGraph(Graph graph, IJavaElement element) throws FileNotFoundException {
		final var elementFile = new ElementFile(element);
		final var elementReader = new ElementFileReader(elementFile);
		final var elementAbsolutePath = elementFile.getAbsolutePath();
		final var elementName = elementFile.getName();
		final var elementJavaReader = new GraphJavaReader(graph, this, elementReader, elementAbsolutePath, elementName);
		graph = elementJavaReader.read();
		return graph;
	}
}
