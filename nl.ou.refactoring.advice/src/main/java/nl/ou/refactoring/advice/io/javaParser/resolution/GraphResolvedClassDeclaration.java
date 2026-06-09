package nl.ou.refactoring.advice.io.javaParser.resolution;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

/**
 * A resolved Class Declaration from a Refactoring Advice Graph (RAG).
 */
public final class GraphResolvedClassDeclaration implements ResolvedReferenceTypeDeclaration {
	/**
	 * The Class node that represents the Class Declaration.
	 */
	private final GraphNodeClass classNode;

	/**
	 * Initialises a new instance of {@link GraphResolvedClassDeclaration}.
	 * @param classNode The Class node that represents the resolved Class Type Declaration.
	 * @throws ArgumentNullException Thrown if classNode is null.
	 */
	public GraphResolvedClassDeclaration(GraphNodeClass classNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(classNode, "classNode");
		this.classNode = classNode;
	}
	
	/**
	 * Gets the Class node that represents the Class Declaration.
	 * @return The Class node that represents the Class Declaration.
	 */
	public GraphNodeClass getClassNode() {
		return this.classNode;
	}

	@Override
	public String getName() {
		return this.classNode.getClassName();
	}

	@Override
	public Optional<ResolvedReferenceTypeDeclaration> containerType() {
		// TODO implement
		return Optional.empty();
	}

	@Override
	public String getPackageName() {
		return
			this
				.classNode
				.getPackageNode()
				.map((packageNode) -> packageNode.getPackageNameFull())
				.orElse(null);
	}

	@Override
	public String getClassName() {
		// TODO implement for encapsulated classes
		return null;
	}

	@Override
	public String getQualifiedName() {
		// TODO implement
		return null;
	}

	@Override
	public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
		// TODO implement
		return null;
	}

	@Override
	public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
		// TODO implement
		return null;
	}

	@Override
	public List<ResolvedFieldDeclaration> getAllFields() {
		// TODO implement
		return null;
	}

	@Override
	public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
		// TODO implement
		return null;
	}

	@Override
	public Set<MethodUsage> getAllMethods() {
		// TODO implement
		return null;
	}

	@Override
	public boolean isAssignableBy(ResolvedType type) {
		// TODO implement
		return false;
	}

	@Override
	public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
		// TODO implement
		return false;
	}

	@Override
	public boolean hasDirectlyAnnotation(String qualifiedName) {
		// TODO implement
		return false;
	}

	@Override
	public boolean isFunctionalInterface() {
		// TODO implement
		return false;
	}

	@Override
	public List<ResolvedConstructorDeclaration> getConstructors() {
		// TODO implement
		return null;
	}
}