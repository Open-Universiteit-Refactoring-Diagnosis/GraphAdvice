package nl.ou.refactoring.advice.eclipse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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
import org.eclipse.jdt.core.search.IJavaSearchScope;
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
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeExpressionStatement;

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
        if (element instanceof IType) {
        	LOGGER.info(
        		"Java element {} is a Class, so also including generalizations and specializations",
        		element.getElementName()
        	);
        	graph = readElementToGraph(graph, (IType)element);
        	graph = readHierarchy(graph, (IType)element);
        }
        if (element instanceof IMethod) {
        	LOGGER.info(
        		"Java element {} is a Method, so also including Class hierarchy and caller methods",
        		element.getElementName()
        	);
        	final var method = (IMethod)element;
        	final var methodClass = method.getDeclaringType();
        	final var methodClassFullyQualifiedName = methodClass.getFullyQualifiedName();
        	var methodClassNodeOptional = graph.getNode(methodClassFullyQualifiedName, GraphNodeClass.class);
        	if (methodClassNodeOptional.isEmpty()) {
        		graph = readElementToGraph(graph, methodClass);
        	}
        	graph = readHierarchy(graph, methodClass);
        	graph = readCallers(graph, method);
        }
        
        return graph;
	}
	
	private static Graph readCallers(Graph graph, IMethod method) {
		final var countDownLatch = new CountDownLatch(1);
		
		try {
			LOGGER.info("Searching callers of method {}", method.getElementName());
			final var searchPattern = SearchPattern.createPattern(method, IJavaSearchConstants.QUALIFIED_REFERENCE);
			final var methodProject = method.getJavaProject();
			final var searchScope =
				SearchEngine.createJavaSearchScope(
					new IJavaProject[] { methodProject },
					IJavaSearchScope.SOURCES
				);
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
							if (!(callerElement instanceof IJavaElement)) {
								return;
							}
							
							final var callerJavaElement = (IJavaElement)callerElement;
							final var callerJavaElementProject = callerJavaElement.getJavaProject();
							if (callerJavaElementProject == null || !methodProject.equals(callerJavaElementProject)) {
								return;
							}
							
							final var callerJavaElementProjectResource = callerJavaElementProject.getResource();
							if (callerJavaElementProjectResource == null) {
								return;
							}
							
							final var callerJavaElementProjectRelativePath =
								callerJavaElementProjectResource
									.getProjectRelativePath()
									.toString();
							if (callerJavaElementProjectRelativePath == null) {
								return;
							}
							
							final var methodProjectRelativePath =
								methodProject
									.getResource()
									.getProjectRelativePath()
									.toString();
							if (!methodProjectRelativePath.equals(callerJavaElementProjectRelativePath)) {
								return;
							}
							
							switch (callerJavaElement) {
								case IMethod callerMethod: {
									final var callerMethodClassName = callerMethod.getDeclaringType().getFullyQualifiedName();
									final var callerMethodClassNodeOptional = graph.getNode(callerMethodClassName, GraphNodeClass.class);
									GraphNodeClass callerMethodClassNode;
									if (callerMethodClassNodeOptional.isEmpty()) {
										append(graph, callerJavaElement);
										callerMethodClassNode = graph.getNode(callerMethodClassName, GraphNodeClass.class).get();
									} else {
										callerMethodClassNode = callerMethodClassNodeOptional.get();
									}
									final var callerMethodNode =
										callerMethodClassNode
											.getOperationNode(
												callerMethod.getElementName(),
												new ArrayList<>() // TODO parameters
											)
											.get();
									final var callerMethodStatements = callerMethodNode.getBody().getStatements();
									for (final var callerMethodStatement : callerMethodStatements) {
										if (callerMethodStatement instanceof GraphNodeExpressionStatement) {
											final var expressionStatement = (GraphNodeExpressionStatement)callerMethodStatement;
											final var statementExpression = expressionStatement.getExpression();
											if (statementExpression instanceof GraphNodeMethodInvocationExpression) {
												final var methodInvocationExpression = (GraphNodeMethodInvocationExpression)statementExpression;
												final var invokedOperationNode = methodInvocationExpression.getInvokedOperationNode();
											}
										}
									}
									break;
								}
								default: {
									break;
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							LOGGER.error("Accepting caller method failed", ex);
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
			LOGGER.error("Failed to read method callers for method " + method.getElementName(), ex);
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
	
	private static Graph readHierarchy(final Graph graph, IType type)
			throws
				JavaModelException,
				FileNotFoundException {
		final var classNodeBaseOptional =
			graph.getNode(type.getFullyQualifiedName(), GraphNodeClass.class);
		final var classNodeBase =
			classNodeBaseOptional
				.orElseGet(() -> {
					try {
						readElementToGraph(graph, type);
						return graph.getNode(type.getFullyQualifiedName(), GraphNodeClass.class).get();
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				});
		final var typeHierarchy = type.newTypeHierarchy(null);
		
		final var typeSupertypes = typeHierarchy.getAllSupertypes(type);
		for (final var typeSupertype : typeSupertypes) {
			if (typeSupertype.getResource() == null) {
				// External Class.
				continue;
			}
			
			try {
				readElementToGraph(graph, typeSupertype);
				if (typeSupertype.isClass()) {
					final var classNode =
						graph
							.getNode(typeSupertype.getFullyQualifiedName(), GraphNodeClass.class)
							.get();
					classNodeBase.is(classNode);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				LOGGER.error("Failed to read supertype to graph", ex);
			}
		}
		
		final var typeSubtypes = typeHierarchy.getAllSubtypes(type);
		for (final var typeSubtype : typeSubtypes) {
			try {
				readElementToGraph(graph, typeSubtype);
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
