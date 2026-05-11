package nl.ou.refactoring.advice.eclipse;

import java.io.FileNotFoundException;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReader;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

/**
 * Builds refactoring graphs from Eclipse contexts.
 */
final class RefactoringGraphBuilder {
	private RefactoringGraphBuilder() { }
	
	/**
	 * Builds a Refactoring Advice Graph (RAG) from a {@link IJavaElement}.
	 * @param element The Java element from which to build a Refactoring Advice Graph (RAG).
	 * @return The Refactoring Advice Graph (RAG).
	 * @throws ArgumentNullException Thrown if element is null.
	 * @throws ElementResourceNotFoundException Thrown if the element's resource could not be found.
	 * @throws ElementResourceLocationNotFoundException Thrown if the element's resource location could not be found.
	 * @throws FileNotFoundException Thrown if the element's file could not be found.
	 * @throws JavaModelException Thrown if determining a SourceType element's super class failed.
	 */
	static final Graph build(IJavaElement element)
			throws
				ArgumentNullException,
				ElementResourceNotFoundException,
				ElementResourceLocationNotFoundException,
				FileNotFoundException,
				JavaModelException {
		ArgumentGuard.requireNotNull(element, "element");
		
		var graph = new Graph(element.getElementName());
		graph = readElementToGraph(graph, element);		
        if (element instanceof IType) {
        	final var type = (IType)element;
    		final GraphNodeClass classNodeBase =
    			graph
    				.getNode(type.getFullyQualifiedName(), GraphNodeClass.class)
    				.get();
        	final var typeHierarchy = type.newSupertypeHierarchy(null);
        	final var typeSuperTypes = typeHierarchy.getAllSupertypes(type);
        	for (final var typeSuperType : typeSuperTypes) {
        		try {
        			graph = readElementToGraph(graph, typeSuperType);
        			if (typeSuperType.isClass()) {
        				final var classNode =
        					graph
        						.getNode(typeSuperType.getFullyQualifiedName(), GraphNodeClass.class)
        						.get();
        				classNodeBase.is(classNode);
        			}
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
        	}
        }
        
        return graph;
	}

	private static Graph readElementToGraph(Graph graph, IJavaElement element)
			throws FileNotFoundException {
		final var elementFile = new ElementFile(element);
        final var elementReader = new ElementFileReader(elementFile);
        final var elementAbsolutePath = elementFile.getAbsolutePath();
        final var elementName = elementFile.getName();
        final var elementJavaReader =
        	new GraphJavaReader(
        		graph,
        		elementReader,
        		elementAbsolutePath,
        		elementName
        	);
        graph = elementJavaReader.read();
		return graph;
	}
}
