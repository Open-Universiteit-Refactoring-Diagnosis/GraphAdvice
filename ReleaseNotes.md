# Open Universiteit Refactoring Advice: Release Notes

[Back to README](/README.md)

## 0.6.0

- Added the Program Location node that can be associated with code (AST) nodes.
- Reorganised and expanded the software documentation.
- Introduced the NLPProvider abstract class for providing natural language refactoring advice.
- Introduced the NLPConcatenationProvider implementation of NLPProvider for providing natural language refactoring advice by concatenating phrase elements that are collected through a Refactoring Advice Graph's nodes.
- Introduced the GraphHtmlWriter for writing refactoring advice with HTML.
- Introduced the GraphHtmlTextWriter implementation of GraphHtmlWriter for writing refactoring advice in natural language with HTML, including links to referred syntax elements.

## 0.5.0

- Added code nodes for representing field access.
- Introduce formal Identifier node for code symbols such as attributes/fields and operations/methods.
- Operation Parameters now (also) a chain of nodes.
- Packages are now trees within the Refactoring Advice Graph.
- Get nodes from a graph by a path specification.
- Several fixes and enhancements in Mermaid Flowcharts and PlantUML Class Diagram writing.

## 0.4.0

- Breaking change: moved code nodes that concern classes to their own classes name space.
- Breaking change: moved code nodes that concern operations to their own operations name space.
- Expanded the Refactoring Advice Graph model with more fine-grained code nodes such as blocks inside an operation and statements inside a block.
- Improved the PlantUML Class Diagram feature: now also supports inner classes.
- Added support for risk Changed Nested Relationship.
- Added test case for the "Move Method" refactoring, including JSON and required fixes in the model and flowchart, UML generation.
- Added JSON Schema generation for serialisation and deserialisation of representations of Refactoring Advice Graphs in JSON.

## 0.3.0

- Ensured that Javadoc is included in the Maven Package for distribution.
- Modified PlantUML Class Diagrams to show whether items have been added and whether they carry a risk.
- Added a feature for writing RAGs as a text advice, using concatenation of phrase parts.

## 0.0.2

- Enhanced feature for writing RAGs to Mermaid Class Diagrams.
- Added a feature for writing RAGs to PlantUML Class Diagrams.
- Added Risk nodes.
- Localisations of node captions.

## 0.0.1

- Initial project setup.
- Added a Refactoring Advice Graph model.
- Added the Force Directed Layout algorithm for drawing RAGs.
- Added an unfinished implementation of a Global Ranking Layout algorithm for drawing RAGs.
- Added a feature for drawing RAGs to an image and/or writing them to an image file.
- Added a feature for reading RAGs from JSON.
- Added a feature for writing RAGs to JSON.
- Added a feature for writing RAGs to Mermaid Class Diagrams.
- Added a feature for writing RAGs to Mermaid Flowcharts.