package nl.ou.refactoring.advice.tests;

public class Employee extends LegacyEmployee {
	private String name;
	private double monthlySalary;
	private String workplace;

	public Employee(String name, double monthlySalary, String workplace) {
		super(name, monthlySalary);
		this.name = name;
		this.monthlySalary = monthlySalary;
		this.workplace = workplace;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public double getMonthlySalary() {
		return this.monthlySalary;
	}
	public void setMonthlySalary(double value) {
		this.monthlySalary = value;
	}
	
	public String getWorkplace() {
		return this.workplace;
	}
	public void setWorkplace(String value) {
		this.workplace = value;
	}
}