package nl.ou.refactoring.advice.nodes.code.operations;

/**
 * A signature of an operation parameter.
 * @param name The name of the operation parameter.
 * @param typeName The name of the operation parameter's type.
 */
public final record GraphNodeOperationParameterSignature(String name, String typeName) { }
