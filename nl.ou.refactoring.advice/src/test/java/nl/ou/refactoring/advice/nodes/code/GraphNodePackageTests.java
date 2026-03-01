package nl.ou.refactoring.advice.nodes.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

public final class GraphNodePackageTests {
	@DisplayName("Should parse a package name and construct a package tree accordingly")
	@Test
	public void getPackageNameFullTest() {
		// Arrange
		final var graph = new Graph("Package Parse Test");
		final var nlPackage = new GraphNodePackage(graph, new GraphNodeIdentifier(graph, "nl"));
		final var ouPackage = new GraphNodePackage(graph, new GraphNodeIdentifier(graph, "ou"));
		final var refactoringPackage = new GraphNodePackage(graph, new GraphNodeIdentifier(graph, "refactoring"));
		nlPackage.has(ouPackage);
		ouPackage.has(refactoringPackage);
		
		// Act
		final var packageNameFull = refactoringPackage.getPackageNameFull();
		final var packageName = refactoringPackage.getPackageName();
		
		// Assert
		assertEquals("nl.ou.refactoring", packageNameFull);
		assertEquals("refactoring", packageName);
	}
	
	@DisplayName("Should parse a package name and construct a package tree accordingly")
	@Test
	public void parseTest() {
		// Arrange
		final var graph = new Graph("Package Parse Test");
		
		// Act
		final var packageNodeRoot = GraphNodePackage.parse(graph, "nl.ou.refactoring.packages.parseTest");
		
		// Assert
		assertNotNull(packageNodeRoot);
	}
}
