package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import dao.DAOFactory;
import dao.SellerDAO;
import entities.Department;
import entities.Seller;

public class SellerTests {
	
	private SellerDAO sellerDAO = DAOFactory.createSellerDAO();

	@Test
	public void mustGetSellerById() {		
		Seller seller = sellerDAO.findById(1);
		System.out.println(seller);
		assertEquals("Updated JUnit Test", seller.getName());		
	}
	
	@Test
	public void mustGetSellerByDepartment() {
		Department department = new Department(2, null);
		List<Seller> sellersByDepartment = sellerDAO.findByDepartment(department);
		for (Seller sellerByDepartment : sellersByDepartment) {
			System.out.println(sellerByDepartment);
		}
		assertTrue(sellersByDepartment.size() > 0);
	}
	
	@Test
	public void mustSellerFindAll() {
		List<Seller> sellersAll = sellerDAO.findAll();
		for (Seller sellerAll : sellersAll) {
			System.out.println(sellerAll);
		}
		assertTrue(sellersAll.size() > 0);
	}
	
	@Test
	public void mustInsertSeller() {
		Department department = new Department(2, null);
		Seller newSeller = new Seller(null, "Insert JUnit Test", "insert@must.com", new Date(), 10000.0, department);
		sellerDAO.insert(newSeller);	
		System.out.println("New Seller: " + newSeller);
		assertTrue(newSeller != null);
	}
	
	@Test
	public void mustUpdateSeller() {
		Seller updatedSeller = sellerDAO.findById(1);
		updatedSeller.setName("Updated JUnit Test");
		sellerDAO.update(updatedSeller);	
		System.out.println("Updated Seller: " + updatedSeller);
		assertEquals("Updated JUnit Test", updatedSeller.getName());
	}
	
	@Test
	public void mustDeleteSeller() {
		sellerDAO.deleteById(3);		
		System.out.println("Delete completed!");
		Seller deletedSeller = sellerDAO.findById(3);
		assertTrue(deletedSeller == null);
	}

}
