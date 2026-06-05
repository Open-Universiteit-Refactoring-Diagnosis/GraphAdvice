package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * A node in a Refactoring Advice Graph that represents the location in a software program's source code.
 */
public final class GraphNodeProgramLocation extends GraphNodeBase {
	/**
	 * The name of the source code file.
	 */
	private final String fileName;
	
	/**
	 * The full name of the source code file, including its path.
	 */
	private final String fileNameFull;
	
	/**
	 * The line number in the source code file where the symbol starts.
	 */
	private final int lineNumberStart;
	
	/**
	 * The column index on the line in the source code file.
	 */
	private final int columnIndexStart;
	
	/**
	 * The line number in the source code file where the symbol starts.
	 */
	private final int lineNumberEnd;
	
	/**
	 * The column index on the line in the source code file where the symbol ends..
	 */
	private final int columnIndexEnd;

	/**
	 * Initialises a new instance of {@link GraphNodeProgramLocation}.
	 * @param graph The Refactoring Advice Graph that contains the node.
	 * @param fileNameFull The full name of the source code file, including its path.
	 * @param fileName The name of the source code file.
	 * @param lineNumberStart The line number in the source code file where the associated symbol starts.
	 * @param columnIndexStart The column index on the line in the source code file at which the associated symbol starts.
	 * @param lineNumberEnd The line number in the source code file where the symbol ends.
	 * @param columnIndexEnd The column index on the line in the source code file at which the associated symbol ends.
	 * @throws ArgumentNullException Thrown if fileName is null.
	 * @throws ArgumentEmptyException Thrown if fileName is empty or contains only white spaces.
	 * @throws IllegalArgumentException Thrown if lineNumber or columnIndex have a value less than 0.
	 */
	public GraphNodeProgramLocation(
		Graph graph,
		String fileNameFull,
		String fileName,
		int lineNumberStart,
		int columnIndexStart,
		int lineNumberEnd,
		int columnIndexEnd
	) throws
			ArgumentNullException,
			ArgumentEmptyException,
			IllegalArgumentException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(fileNameFull, "fileNameFull");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(fileName, "fileName");
		ArgumentGuard.requireGreaterThanOrEqual(0, lineNumberStart, "lineNumberStart");
		ArgumentGuard.requireGreaterThanOrEqual(0, columnIndexStart, "columnIndexStart");
		ArgumentGuard.requireGreaterThanOrEqual(0, lineNumberStart, "lineNumberEnd");
		ArgumentGuard.requireGreaterThanOrEqual(0, columnIndexStart, "columnIndexEnd");
		this.fileNameFull = fileNameFull;
		this.fileName = fileName;
		this.lineNumberStart = lineNumberStart;
		this.columnIndexStart = columnIndexStart;
		this.lineNumberEnd = lineNumberEnd;
		this.columnIndexEnd = columnIndexEnd;
	}
	
	/**
	 * Gets the full name of the source code file, including its path.
	 * @return The full name of the source code file, including its path.
	 */
	public String getFileNameFull() {
		return this.fileNameFull;
	}
	
	/**
	 * Gets the name of the source code file.
	 * @return The name of the source code file.
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * Gets the line number in the source code file where the symbol starts.
	 * @return The line number in the source code file where the symbol starts.
	 */
	public int getLineNumberStart() {
		return this.lineNumberStart;
	}
	
	/**
	 * Gets the column index on the line in the source code file where the symbol starts.
	 * @return The column index on the line in the source code file where the symbol starts.
	 */
	public int getColumnIndexStart() {
		return this.columnIndexStart;
	}
	
	/**
	 * Gets the line number in the source code file where the symbol ends.
	 * @return The line number in the source code file where the symbol ends.
	 */
	public int getLineNumberEnd() {
		return this.lineNumberEnd;
	}
	
	/**
	 * Gets the column index on the line in the source code file where the symbol ends.
	 * @return The column index on the line in the source code file where the symbol ends.
	 */
	public int getColumnIndexEnd() {
		return this.columnIndexEnd;
	}

	@Override
	public String getLabel() {
		return ResourceProvider.GraphNodeLabels.getLabel(this.getClass());
	}
	
	@Override
	public String getCaption() {
		return String.format(
			"%s %d:%d-%d:%d",
			this.fileName,
			this.lineNumberStart,
			this.columnIndexStart,
			this.lineNumberEnd,
			this.columnIndexEnd
		);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		return new GraphNodeProgramLocation(
			graph,
			this.fileNameFull,
			this.fileName,
			this.lineNumberStart,
			this.columnIndexStart,
			this.lineNumberEnd,
			this.columnIndexEnd
		);
	}
	
	@Override
	public boolean equals(GraphNode other) {
		if (!(other instanceof GraphNodeProgramLocation)) {
			return false;
		}
		final var programLocationOther = (GraphNodeProgramLocation)other;
		return
			this.fileNameFull.equals(programLocationOther.fileNameFull) &&
			this.fileName.equals(programLocationOther.fileName) &&
			this.lineNumberStart == programLocationOther.lineNumberStart &&
			this.columnIndexStart == programLocationOther.columnIndexStart &&
			this.lineNumberEnd == programLocationOther.lineNumberEnd &&
			this.columnIndexEnd == programLocationOther.columnIndexEnd;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", this.getLabel(), this.getCaption());
	}
}