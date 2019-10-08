package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import dao.DAOFactory;
import dao.DepartmentDAO;
import entities.Department;

public class DepartmentTests {
	
	private DepartmentDAO departmentDAO = DAOFactory.createDepartmentDAO();

	@Test
	public void mustGetDepartmentById() {		
		Department department = departmentDAO.findById(1);
		System.out.println(department);
		assertEquals("Computers", department.getName());		
	}
	
	@Test
	public void mustGetAllDepartments() {
		List<Department> departments = departmentDAO.findAll();
		for (Department department : departments) {
			System.out.println(department);
		}
		assertTrue(departments.size() > 0);
	}
	
	@Test
	public void mustInsertOneDepartment() {
		Department department = new Department(null, "Insert By JUnit2");
		departmentDAO.insert(department);
		System.out.println(department);
		assertTrue(department != null);
	}
	
	@Test
	public void mustUpdateADepartment() {
		Department department = departmentDAO.findById(3);
		department.setName("Updated By JUnit");
		departmentDAO.update(department);
		System.out.println(department);
		assertEquals("Updated By JUnit", department.getName());
	}
	

	@Test
	public void mustDeleteDepartmentById() {
		departmentDAO.deleteById(6);
		System.out.println("Delete completed");
		Department department = departmentDAO.findById(6);
		assertTrue(department == null);		
	}

}
