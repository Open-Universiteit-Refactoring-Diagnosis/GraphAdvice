# Open Universiteit Refactoring Advice

A software component for assembling a refactoring graph and converting the graph to a refactoring advice.

## Release Notes

Please find the Release Notes [here](/ReleaseNotes.md).

## Importing the software component in your project

### Maven

To add Open Universiteit Refactoring Advice in your software project that supports Maven, please add the following in the `pom.xml` file:
```xml
<dependency>
  <groupId>nl.ou.refactoring</groupId>
  <artifactId>nl.ou.refactoring.advice</artifactId>
  <version>[0.0.1,)</version>
</dependency>
```
You may decide how to specify the version, but it is recommended to always keep using the latest version. If breaking changes are introduced, it is recommended to keep using the latest minor version until your software has been adapted to the breaking changes.

If you wish to use the GitHub feed, please add the following in either your `pom.xml` or `settings.xml`:
```xml
<repository>
	<id>github</id>
	<url>https://maven.pkg.github.com/Open-Universiteit-Refactoring-Diagnosis/GraphAdvice</url>
	<snapshots>
		<enabled>true</enabled>
	</snapshots>
</repository>
```
You may decide which `id` to use or whether to support snapshots.

## Creating a Refactoring Advice Graph

```java
package example;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyChooseDifferentName;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

class Example {
    static final void main() {
        // Create a new Refactoring Advice Graph with the name "My Graph".
        final var graph = new Graph("My Graph");

        // WORKFLOW: MICROSTEPS
        /*
         * Create a start node for the Refactoring Advice Graph.
         * All RAGs require exactly (not more and not fewer than) one start node.
         */
        final var graphStart = graph.start();
        /*
        * Create a microstep for adding a new method to a class.
        * Each node in the RAG «must» be constructed with the owner Graph because a node can and must belong to only one Graph.
        * The constructor already adds the node to the Graph, so it will not be necessary to call the respective method on the Graph itself.
        */
        final var addMethod = new GraphNodeMicrostepAddMethod(graph);
        /*
         * The start node initiates the first microstep.
         * Relational methods produce the edge that they add to the Graph, but the edge can be ignored if it will not be used immediately.
         */
        graphStart.initiates(addMethod);
        /*
         * Create another microstep for removing an expression from a statement in a code block.
         * The node must belong to the same Graph.
         */
        final var removeExpression = new GraphNodeMicrostepRemoveExpression(graph);
        // The Add Method microstep precedes the Remove Expression microstep.
        addMethod.precedes(removeExpression);
        /*
         * You may continue adding more microsteps
         * ...
         * ...
         * until the last microstep:
         */
        final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
        /*
         * The 'finalises' edge loops the last microstep back to the graph's start node.
         * It is therefore a requirement that the start node has already been added to the Graph.
         * The finalisation indicates that no more microsteps will follow and the workflow of the refactoring is complete.
         */
        removeMethod.finalises();

        // WORKFLOW: RISKS AND DANGERS
        /*
         * Add a "Missing Definition" risk to the Graph.
         * Please refer to the literature for the meaning of a Missing Definition risk/danger.
         */
        final var missingDefinition = new GraphNodeRiskMissingDefinition(graph);
        /*
         * Some microsteps cause the Missing Definition, others make it obsolete.
         * Whenever a risk is made obsolete, it will not be considered a danger in the refactoring advice anymore.
         */
        addMethod.obsolesces(missingDefinition);
        removeExpression.obsolesces(missingDefinition);
        // ...
        removeMethod.causes(missingDefinition);
        /*
         * Add a "Double Definition" risk to the Graph.
         * Please refer to the literature for the meaning of a Double Definition risk/danger.
         */
        final var doubleDefinition = new GraphNodeRiskDoubleDefinition(graph);
        /*
         * Add Method causes a Double Definition in this particular refactoring.
         * None of the other microsteps make this risk obsolete, so this risk will be promoted to a danger in the refactoring advice.
         */
        addMethod.causes(doubleDefinition);

        // CODE
        /*
         * Add a package called "org.example" to the refactoring advice.
         */
        final var packageExample = new GraphNodePackage(graph, "org.example");
        /*
         * Add a class called "Alpha" to the refactoring advice.
         */
        final var classAlpha = new GraphNodeClass(graph, "Alpha");
        /*
         * Class "org.example.Alpha" belongs to the package "org.example".
         */
        packageExample.has(classAlpha);
        /*
         * Add an attribute and an operation to the class.
         */
        final var attributeFoo = new GraphNodeAttribute(graph, "foo");
        classAlpha.has(attributeFoo);
        final var operationAbc = new GraphNodeOperation(graph, "abc");
        classAlpha.has(operationAbc);
        /*
         * Let the refactoring advice know that the Double Definition risk/danger affects the "abc()" operation on class "org.example.Alpha".
         */
        doubleDefinition.affects(operationAbc);

        // WORKFLOW: REMEDIES
        /*
         * As part of the refactoring advice, provide a remedy such as "Choose a Different Name".
         * In this case, this would mitigate the Double Definition danger.
         * Multiple remedies may exist for the same danger. The user will choose which one to apply.
         */
        final var remedyChooseDifferentName = new GraphNodeRemedyChooseDifferentName(graph);
        remedyChooseDifferentName.mitigates(doubleDefinition);
    }
}
```


## Adding a new Microstep to the model

In the `nl.ou.refactoring.advice.nodes.workflow.microsteps` package, add a new class that extends the `GraphNodeMicrostep` class:

```java
/**
 * Represents a Microstep in a Refactoring Advice Graph that provides as an example for documentation.
 */
public final class GraphNodeMicrostepExample extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepExample}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepExample(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}
```

Generally, the caption of graph nodes is read from resources to enable multilingual support.
*GraphNodeCaptions.properties*
```
GraphNodeMicrostepExample=Example
```

*GraphNodeCaptions_nl_NL.properties*
```
GraphNodeMicrostepExample=Voorbeeld
```

Optionally, the method `getCaption` can be overridden so the resource is not necessarily read anymore.
```java
/**
 * Represents a Microstep in a Refactoring Advice Graph that provides as an example for documentation.
 */
public final class GraphNodeMicrostepExample extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepExample}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepExample(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	@Override
	public String getCaption() {
		return "Example";
	}
}
```


## Adding a new Risk to the model

In the `nl.ou.refactoring.advice.nodes.workflow.risks` package, add a new class that extends the `GraphNodeRisk` class:

```java
/**
 * Represents an "Example" risk in a Refactoring Advice Graph.
 * This risk may arise if an example is used in the documentation to illustrate how a new risk node is added to the model.
 */
public final class GraphNodeRiskExample extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskExample}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskExample(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Example" risk affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Example" risk and the affected Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
}
```

In this example the risk affects an 'operation' in the code section of the model.
Any combination of code nodes may be affected, depending on the nature of the refactoring risk.
For these combinations an `affects` overload must be implemented for each relationship.

Analogous to a new `Microstep`, the `getCaption` method must either be provided with a new resource entry or an overriding implementation.


## Adding a new Remedy to the model

In the `nl.ou.refactoring.advice.nodes.workflow.remedies` package, add a new class that extends the `GraphNodeRemedy` class:

```java
/**
 * Represents a remedy of providing an example in documentation on how to add a new remedy to the graph model.
 */
public final class GraphNodeRemedyExample extends GraphNodeRemedy {
	/**
	 * Initialises a new instance of {@link GraphNodeRemedyExample}.
	 * @param graph {@link Graph} The graph that contains the remedy.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRemedyExample(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the Example remedy mitigates the Example risk.
	 * @param doubleDefinition The Example risk that is mitigated by this remedy.
	 * @return An edge that connects the remedy and the risk.
	 * @throws ArgumentNullException Thrown if doubleDefinition is null.
	 */
	public GraphEdgeMitigates mitigates(GraphNodeRiskExample example)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				example,
				(source, destination) -> new GraphEdgeMitigates(source, destination),
				GraphEdgeMitigates.class);
	}
}
```

In this example the remedy mitigates an 'Example' risk.
Any combination of risk nodes may be mitigated, depending on the nature of the refactoring remedy.
For these combinations a `mitigates` overload must be implemented for each relationship.

Analogous to a new `Microstep`, the `getCaption` method must either be provided with a new resource entry or an overriding implementation.