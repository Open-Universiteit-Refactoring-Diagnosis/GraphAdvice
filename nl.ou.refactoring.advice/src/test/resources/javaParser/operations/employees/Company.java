package nl.ou.refactoring.advice.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Company {
	private final Employee director;
	private final Set<Employee> employees;
	
	public Company() {
		this.director = new Employee("Directeur", 10000D, "Directie");
		this.employees = new HashSet<Employee>();
	}
	
	public String getDirectorName() {
		this.director.getName();
		return "test";
	}
	
	public List<String> getEmployeeNames() {
		final var result = new ArrayList<String>();
		for (final var employee : this.employees) {
			employee.getName();
			final var employeeName = employee.getName();
			result.add(employeeName);
		}
		return Collections.unmodifiableList(result);
	}
}