package nl.ou.refactoring.advice.io.javaParser.resolution;

import java.util.List;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

/**
 * A resolved Method Declaration in a Refactoring Advice Graph (RAG).
 */
public final class GraphResolvedMethodDeclaration implements ResolvedMethodDeclaration {
	/**
	 * The Operation Node that represents the resolved Method Declaration.
	 */
	private final GraphNodeOperation operationNode;
	
	/**
	 * Initialises a new instance of {@link GraphResolvedMethodDeclaration}.
	 * @param operationNode The Operation Node that represents the resolved Method Declaration.
	 */
	public GraphResolvedMethodDeclaration(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(operationNode, "operationNode");
		this.operationNode = operationNode;
	}
	
	/**
	 * Gets the Operation Node that represents the resolved Method Declaration.
	 * @return The Operation Node that represents the resolved Method Declaration.
	 */
	public GraphNodeOperation getOperationNode() {
		return this.operationNode;
	}
	
	@Override
	public ResolvedReferenceTypeDeclaration declaringType() {
		return
			this
				.operationNode
				.getClassNode()
				.map((classNode) -> new GraphResolvedClassDeclaration(classNode))
				.orElse(null);
	}

	@Override
	public int getNumberOfParams() {
		final var operationParameters =
			this
				.operationNode
				.getOperationParameters();
		return operationParameters.size();
	}

	@Override
	public ResolvedParameterDeclaration getParam(int i) {
		// TODO implement
		return null;
	}

	@Override
	public int getNumberOfSpecifiedExceptions() {
		// TODO implement
		return 0;
	}

	@Override
	public ResolvedType getSpecifiedException(int index) {
		// TODO implement
		return null;
	}

	@Override
	public String getName() {
		return this.operationNode.getOperationName();
	}

	@Override
	public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
		// TODO implement
		return null;
	}

	@Override
	public AccessSpecifier accessSpecifier() {
		// TODO implement
		return null;
	}

	@Override
	public ResolvedType getReturnType() {
		// TODO implement
		return null;
	}

	@Override
	public boolean isAbstract() {
		// TODO implement
		return false;
	}

	@Override
	public boolean isDefaultMethod() {
		// TODO implement
		return false;
	}

	@Override
	public boolean isStatic() {
		// TODO implement
		return false;
	}

	@Override
	public String toDescriptor() {
		// TODO implement
		return null;
	}

}
