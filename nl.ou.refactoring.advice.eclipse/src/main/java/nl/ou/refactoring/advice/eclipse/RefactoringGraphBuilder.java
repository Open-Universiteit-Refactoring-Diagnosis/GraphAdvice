package nl.ou.refactoring.advice.eclipse;

import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReader;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

/**
 * Builds Refactoring Advice Graphs (RAGs) from Eclipse contexts.
 */
public final class RefactoringGraphBuilder {
	private static final Logger LOGGER = LogManager.getLogger(RefactoringGraphBuilder.class);
	private RefactoringGraphBuilder() { }
	
	/**
	 * Appends code nodes from a {@link IJavaElement} to a Refactoring Advice Graph (RAG).
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @param element The Java element from which to append code nodes to a Refactoring Advice Graph (RAG).
	 * @return The Refactoring Advice Graph (RAG).
	 * @throws ArgumentNullException Thrown if element is null.
	 * @throws ElementResourceNotFoundException Thrown if the element's resource could not be found.
	 * @throws ElementResourceLocationNotFoundException Thrown if the element's resource location could not be found.
	 * @throws FileNotFoundException Thrown if the element's file could not be found.
	 * @throws JavaModelException Thrown if determining a SourceType element's super class failed.
	 */
	public static final Graph append(Graph graph, IJavaElement element)
			throws
				ArgumentNullException,
				ElementResourceNotFoundException,
				ElementResourceLocationNotFoundException,
				FileNotFoundException,
				JavaModelException {
		ArgumentGuard.requireNotNull(element, "element");
		
		LOGGER.info(
			"Reading Java code from Java element {} to Refactoring Advice Graph code nodes",
			element.getElementName()
		);
		graph = readElementToGraph(graph, element);		
        if (element instanceof IType) {
        	LOGGER.info(
        		"Java element {} is a Class, so also including generalizations and specializations",
        		element.getElementName()
        	);
        	graph = readHierarchy(graph, (IType)element);
        }
        if (element instanceof IMethod) {
        	final var method = (IMethod)element;
        	graph = readHierarchy(graph, method.getDeclaringType());
        	graph = readCallers(graph, method);
        }
        
        return graph;
	}
	
	private static Graph readCallers(Graph graph, IMethod method) {
		final var countDownLatch = new CountDownLatch(1);
		
		try {
			final var searchPattern = SearchPattern.createPattern(method, IJavaSearchConstants.REFERENCES);
			final var project = method.getJavaProject();
			final var searchScope = SearchEngine.createJavaSearchScope(new IJavaProject[] { project });
			final var searchEngine = new SearchEngine();
			searchEngine.search(
				searchPattern,
				new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
				searchScope,
				new SearchRequestor() {
					@Override
					public void acceptSearchMatch(SearchMatch match) {
						try {
							final var callerElement = match.getElement();
							if (callerElement instanceof IJavaElement) {
								append(graph, (IJavaElement)callerElement);
								switch (callerElement) {
									case IMethod callerMethod: {
										
										break;
									}
									default: {
										break;
									}
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					
					@Override
					public void endReporting() {
						super.endReporting();
						countDownLatch.countDown();
					}
				},
				null
			);
			countDownLatch.await(10, TimeUnit.SECONDS);
		} catch (Exception ex) {
			ex.printStackTrace();
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
	
	private static Graph readHierarchy(Graph graph, IType type) throws JavaModelException {
		final GraphNodeClass classNodeBase =
			graph
				.getNode(type.getFullyQualifiedName(), GraphNodeClass.class)
				.get();
		final var typeHierarchy = type.newTypeHierarchy(null);
		
		final var typeSupertypes = typeHierarchy.getAllSupertypes(type);
		for (final var typeSupertype : typeSupertypes) {
			try {
				graph = readElementToGraph(graph, typeSupertype);
				if (typeSupertype.isClass()) {
					final var classNode =
						graph
							.getNode(typeSupertype.getFullyQualifiedName(), GraphNodeClass.class)
							.get();
					classNodeBase.is(classNode);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		final var typeSubtypes = typeHierarchy.getAllSubtypes(type);
		for (final var typeSubtype : typeSubtypes) {
			try {
				graph = readElementToGraph(graph, typeSubtype);
				if (typeSubtype.isClass()) {
					final var classNode =
						graph
							.getNode(typeSubtype.getFullyQualifiedName(), GraphNodeClass.class)
							.get();
					classNode.is(classNodeBase);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return graph;
	}
}
