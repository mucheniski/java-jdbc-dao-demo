package dao;

import java.util.List;

import entities.Department;
import entities.Seller;

public interface SellerDAO {
	
	void insert(Seller seller);
	
	void update(Seller seller);
	
	void deleteById(Integer id);
	
	Seller findById(Integer id);
	
	List<Seller> findAll();
	
	List<Seller> findByDepartment(Department department);

}
