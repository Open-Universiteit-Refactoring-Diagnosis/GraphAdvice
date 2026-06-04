package nl.ou.refactoring.advice.io.javaParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.javaParser.resolution.GraphResolvedMethodDeclaration;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameterSignature;

/**
 * Resolves symbols for the Java Parser from a Refactoring Advice Graph (RAG).
 */
public final class GraphJavaReaderSymbolResolver implements SymbolResolver {
	private static final Logger LOGGER = LogManager.getLogger(GraphJavaReaderSymbolResolver.class);
	private final Graph graph;
	private final GraphJavaReaderResolutionProvider resolutionProvider;

	/**
	 * Initialises a new instance of {@link GraphJavaReaderSymbolResolver}.
	 * @param graph The Refactoring Advice Graph (RAG) that contains relevant Abstract Syntax Tree (AST) symbols.
	 * @param resolutionProvider Resolves additional Graph nodes that may not have been loaded in the Refactoring Advice Graph (RAG) yet.
	 * @exception ArgumentNullException Thrown if graph is null.
	 */
	public GraphJavaReaderSymbolResolver(Graph graph, GraphJavaReaderResolutionProvider resolutionProvider)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(resolutionProvider, "resolutionProvider");
		this.graph = graph;
		this.resolutionProvider = resolutionProvider;
	}
	
	/**
	 * Gets the Refactoring Advice Graph (RAG) that contains relevant Abstract Syntax Tree (AST) symbols.
	 * @return The Refactoring Advice Graph (RAG) that contains relevant Abstract Syntax Tree (AST) symbols.
	 */
	public Graph getGraph() {
		return this.graph;
	}
	
	private static <T extends Node> Optional<T> findAncestor(Node node, Class<T> classType) {
		var parentNodeOptional = node.getParentNode();
		
		while (parentNodeOptional.isPresent()) {
			final var parentNode = parentNodeOptional.get();
			if (classType.isInstance(parentNode)) {
				return Optional.of(classType.cast(parentNode));
			}
			parentNodeOptional = parentNode.getParentNode();
		}
		
		return Optional.empty();
	}
	
	private static List<GraphNodeOperationParameterSignature> toParameterSignatures(NodeList<Expression> methodArguments) {
		final var result = new ArrayList<GraphNodeOperationParameterSignature>();
		for (final var expression : methodArguments) {
			switch (expression) {
				default: {
					// Unsupported expression.
					break;
				}
			}
		}
		return result;
	}

	@Override
	public <T> T resolveDeclaration(Node node, Class<T> resultClass) throws UnsolvedSymbolException {
		if (node instanceof MethodCallExpr) {
			final var methodCallExpression = (MethodCallExpr)node;			
			
			final var methodName = methodCallExpression.getNameAsString();
			final var methodArgumentSignatures = toParameterSignatures(methodCallExpression.getArguments());
			
			// Resolve receiver Class
			final var receiverClassFullyQualifiedName = this.resolveReceiverClassFullyQualifiedName(methodCallExpression);
			var receiverClassNodeOptional = this.graph.getNode(receiverClassFullyQualifiedName, GraphNodeClass.class);
			if (receiverClassNodeOptional.isEmpty()) {
				LOGGER.info("Attempting to resolve Class Node by Fully Qualified Name '{}'", receiverClassFullyQualifiedName);
				receiverClassNodeOptional =
					this
						.resolutionProvider
						.resolveByFullyQualifiedName(
							this.graph,
							receiverClassFullyQualifiedName,
							GraphNodeClass.class
						);
				if (receiverClassNodeOptional.isEmpty()) {
					throw new UnsolvedSymbolException(methodName);
				}
			}
			final var receiverClassNode = receiverClassNodeOptional.get();
			
			// Resolve receiver Operation
			final var receiverOperationNodeOptional = receiverClassNode.getOperationNode(methodName, methodArgumentSignatures);
			if (receiverOperationNodeOptional.isEmpty()) {
				throw new UnsolvedSymbolException(methodName);
			}
			
			return resultClass.cast(new GraphResolvedMethodDeclaration(receiverOperationNodeOptional.get()));
		}
		
		LOGGER.warn("Unsupported node {} for declaration resolution", node.getClass().getName());
		return null;
	}
	
	private String resolveReceiverClassFullyQualifiedName(MethodCallExpr methodCallExpression, Expression expression) throws UnsolvedSymbolException {
		return switch (expression) {
			case FieldAccessExpr fieldAccessExpression ->
				this.resolveReceiverClassFullyQualifiedName(
					methodCallExpression,
					fieldAccessExpression
				);
			case MethodCallExpr innerMethodCallExpression -> {
				final var innerMethod = this.resolveDeclaration(innerMethodCallExpression, GraphResolvedMethodDeclaration.class);
				yield innerMethod.getReturnType().toString();
			}
			case NameExpr nameExpression ->
				this.resolveReceiverClassFullyQualifiedName(
					methodCallExpression,
					nameExpression
				);
			case ObjectCreationExpr objectCreationExpression ->
				objectCreationExpression.getType().getNameAsString();
			case ThisExpr thisExpression ->
				this.resolveReceiverClassFullyQualifiedName(
					methodCallExpression,
					thisExpression
				);
			default -> throw new UnsolvedSymbolException(methodCallExpression.getNameAsString());
		};
	}
	
	private String resolveReceiverClassFullyQualifiedName(
		MethodCallExpr methodCallExpression,
		FieldAccessExpr fieldAccessExpression
	) throws UnsolvedSymbolException {
		final var fieldAccessScope = fieldAccessExpression.getScope();
		if (fieldAccessScope instanceof ThisExpr) {
			final var ownerClassOptional = findAncestor(fieldAccessExpression, ClassOrInterfaceDeclaration.class);
			if (ownerClassOptional.isEmpty()) {
				throw new UnsolvedSymbolException(methodCallExpression.getNameAsString());
			}
			
			final var ownerClass = ownerClassOptional.get();
			final var fieldDeclarationOptional = ownerClass.getFieldByName(fieldAccessExpression.getNameAsString());
			if (fieldDeclarationOptional.isEmpty()) {
				throw new UnsolvedSymbolException(methodCallExpression.getNameAsString());
			}
			
			final var fieldDeclaration = fieldDeclarationOptional.get();
			final var fieldDeclarationType = fieldDeclaration.getElementType();
			return this.resolveTypeToFullyQualifiedName(ownerClass, fieldDeclarationType);
		}
		
		return this.resolveReceiverClassFullyQualifiedName(methodCallExpression, fieldAccessExpression.getScope());
	}
	
	private String resolveTypeToFullyQualifiedName(ClassOrInterfaceDeclaration contextClass, Type type) throws UnsolvedSymbolException {
		return switch (type) {
			case ClassOrInterfaceType classOrInterfaceType -> {
				final var nameWithScope = classOrInterfaceType.getNameWithScope();
				if (nameWithScope.contains(".")) {
					yield nameWithScope;
				}
				
				final var simpleName = classOrInterfaceType.getNameAsString();
				final var compilationUnit =
					contextClass
						.findCompilationUnit()
						.orElseThrow(() -> new UnsolvedSymbolException(type.toString()));
				final var imports = compilationUnit.getImports();
				for (final var importDeclaration : imports) {
					if (importDeclaration.isStatic()) {
						continue;
					}
					if (importDeclaration.getName().getIdentifier().equals(simpleName)) {
						yield importDeclaration.getName().toString();
					}
				}
				
				final var packageDeclaration =
					compilationUnit
						.getPackageDeclaration()
						.orElseThrow(() -> new UnsolvedSymbolException(type.toString()));
				yield packageDeclaration.getNameAsString() + "." + simpleName;
			}
			default -> throw new UnsolvedSymbolException(type.toString());
		};
	}
	
	private String resolveReceiverClassFullyQualifiedName(MethodCallExpr methodCallExpression, NameExpr nameExpression)
			throws UnsolvedSymbolException {
		final var name = nameExpression.getNameAsString();
		if (Character.isUpperCase(name.charAt(0))) {
			return name; // Static Class
		}
		
		// TODO Variable resolution not implemented yet.
		throw new UnsolvedSymbolException(methodCallExpression.getNameAsString());
	}
	
	private String resolveReceiverClassFullyQualifiedName(MethodCallExpr methodCallExpression, ThisExpr thisExpression)
			throws UnsolvedSymbolException {
		final var declaringClassOptional = findAncestor(methodCallExpression, ClassOrInterfaceDeclaration.class);
		if (declaringClassOptional.isEmpty()) {
			throw new UnsolvedSymbolException(methodCallExpression.getNameAsString());
		}
		
		final var declaringClass = declaringClassOptional.get();
		final var declaringClassFullyQualifiedNameOptional = declaringClass.getFullyQualifiedName();
		if (declaringClassFullyQualifiedNameOptional.isEmpty()) {
			throw new UnsolvedSymbolException(methodCallExpression.getNameAsString());
		}
		
		return declaringClassFullyQualifiedNameOptional.get();
	}
	
	private String resolveReceiverClassFullyQualifiedName(MethodCallExpr methodCallExpression)
			throws UnsolvedSymbolException {
		// Explicit receiver (e.g. `employee.getName()`s)
		if(methodCallExpression.getScope().isPresent()) {
			final var receiver = methodCallExpression.getScope().get();
			return this.resolveReceiverClassFullyQualifiedName(methodCallExpression, receiver);
		}
		
		// Implicit receiver (e.g. `this.getName()`)
		final var enclosingClassOptional = findAncestor(methodCallExpression, ClassOrInterfaceDeclaration.class);
		if (enclosingClassOptional.isEmpty()) {
			throw new UnsolvedSymbolException(methodCallExpression.getNameAsString());
		}
		
		return
			enclosingClassOptional
				.get()
				.getFullyQualifiedName()
				.orElseThrow(() -> new UnsolvedSymbolException(methodCallExpression.getNameAsString()));
	}

	@Override
	public <T> T toResolvedType(Type javaparserType, Class<T> resultClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedType calculateType(Expression expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedReferenceTypeDeclaration toTypeDeclaration(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

}
