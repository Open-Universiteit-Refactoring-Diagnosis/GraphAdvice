package nl.ou.refactoring.advice.io.layouts.forceDirected;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutSettings;

/**
 * Settings for a Force-Directed Layout of a Refactoring Advice Graph.
 */
public final class GraphLayoutForceDirectedSettings extends GraphLayoutSettings {
	private int iterations = 10000;
	private double repulsionConstant = 1000.0;
	private double springLength = 200.0;
	private double springStrength = 0.1;
	private double damping = 0.85;
	private double timeStep = 0.5;
	
	/**
	 * Initialises a new instance of {@link GraphLayoutForceDirectedSettings}.
	 */
	public GraphLayoutForceDirectedSettings() { }

	/**
	 * Gets the number of iterations in the Force-Directed Layout algorithm.
	 * @return The number of iterations in the Force-Directed Layout algorithm.
	 */
	public int getIterations() {
		return this.iterations;
	}
	
	/**
	 * Sets the number of iterations in the Force-Directed Layout algorithm. Must be at least 1.
	 * @param value The number of iterations in the Force-Directed Layout algorithm.
	 * @throws IllegalArgumentException Thrown if the value is smaller than 1.
	 */
	public void setIterations(int value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(1, value, null);
		this.iterations = value;
	}
	
	/**
	 * Gets the repulsion constant in the Force-Directed Layout algorithm.
	 * This value determines to what degree nodes repel each other.
	 * @return The repulsion constant in the Force-Directed Layout algorithm.
	 */
	public double getRepulsionConstant() {
		return this.repulsionConstant;
	}
	
	/**
	 * Sets the repulsion constant in the Force-Directed Layout algorithm.
	 * This value determines to what degree nodes repel each other.
	 * @param value The new repulsion constant in the Force-Directed Layout algorithm.
	 */
	public void setRepulsionConstant(double value) {
		this.repulsionConstant = value;
	}
	
	/**
	 * Gets the spring length in the Force-Directed Layout algorithm.
	 * This value determines the length at which an edge will pull on a neighbouring node.
	 * @return The spring length in the Force-Directed Layout algorithm.
	 */
	public double getSpringLength() {
		return this.springLength;
	}
	
	/**
	 * Sets the spring length in the Force-Directed Layout algorithm.
	 * This value determines the length at which an edge will pull on a neighbouring node.
	 * @param value The new spring length in the Force-Directed Layout algorithm.
	 * @throws IllegalArgumentException Thrown if the value is less than zero.
	 */
	public void setSpringLength(double value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0.0, value, "springLength");
		this.springLength = value;
	}
	
	/**
	 * Gets the spring strength in the Force-Directed Layout algorithm.
	 * This value determines the strength with which an edge will pull on a neighbouring node.
	 * @return The spring strength in the Force-Directed Layout algorithm.
	 */
	public double getSpringStrength() {
		return this.springStrength;
	}
	
	/**
	 * Sets the spring strength in the Force-Directed Layout algorithm.
	 * This value determines the strength with which an edge will pull on a neighbouring node.
	 * @param value The new spring strength in the Force-Directed Layout algorithm.
	 * @throws IllegalArgumentException Thrown if the value is less than zero.
	 */
	public void setSpringStrength(double value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0.0, value, "springStrength");
		this.springStrength = value;
	}
	
	/**
	 * Gets the value with which the velocity of a node are damped.
	 * @return The value with which the velocity of a node are damped.
	 */
	public double getDamping() {
		return this.damping;
	}
	
	/**
	 * Sets the value with which the velocity of a node is damped.
	 * @param value The value with which the velocity and force of a node are damped.
	 */
	public void setDamping(double value) {
		this.damping = value;
	}
	
	/**
	 * Gets the step with which a location of a node is altered by its velocity.
	 * @return The step with which a location of a node is altered by its velocity.
	 */
	public double getTimeStep() {
		return this.timeStep;
	}
	
	/**
	 * Sets the step with which a location of a node is altered by its velocity.
	 * @param value The step with which a location of a node is altered by its velocity.
	 * @throws IllegalArgumentException Thrown if the value is smaller than zero.
	 */
	public void setTimeStep(double value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0.0, value, "timeStep");
		this.timeStep = value;
	}

	@Override
	public GraphLayoutForceDirected createLayout() {
		return new GraphLayoutForceDirected(this);
	}
}
