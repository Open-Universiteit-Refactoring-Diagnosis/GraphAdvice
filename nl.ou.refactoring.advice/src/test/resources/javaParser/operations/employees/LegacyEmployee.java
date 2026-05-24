package nl.ou.refactoring.advice.tests;

public class LegacyEmployee {
	private String name;
	private double monthlySalary;
	
	public LegacyEmployee(String name, double monthlySalary) {
		this.name = name;
		this.monthlySalary = monthlySalary;
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
	
	public void salaryBonus(double bonus) {
		this.monthlySalary = this.monthlySalary + bonus;
	}
}