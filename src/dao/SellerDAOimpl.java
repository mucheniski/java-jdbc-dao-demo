package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBConnection;
import db.DBException;
import entities.Department;
import entities.Seller;

public class SellerDAOimpl implements SellerDAO {
	
	private Connection connection;
	
	public SellerDAOimpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller seller) {
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String sql = "INSERT INTO seller\n" + 
					 "(Name, Email, BirthDate, BaseSalary, DepartmentId)\n" + 
					 "VALUES\n" + 
					 "(?, ?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmail());
			preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary());
			preparedStatement.setInt(5, seller.getDepartment().getId());
			
			int rowsAffect = preparedStatement.executeUpdate();
			
			if (rowsAffect > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();				
				if (resultSet.next()) {
					int id = resultSet.getInt(1);
					seller.setId(id);
				}
				DBConnection.closeResultset(resultSet);
			}
			else {
				throw new DBException("Unexpected error! No rows affected!");
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(preparedStatement);
		}		
	}

	@Override
	public void update(Seller seller) {

		PreparedStatement preparedStatement = null;
		
		try {
			
			String sql = "UPDATE seller\n" + 
						 "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?\n" + 
						 "WHERE Id = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmail());
			preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary());
			preparedStatement.setInt(5, seller.getDepartment().getId());
			
			preparedStatement.setInt(6, seller.getId());
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(preparedStatement);
		}
				
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement preparedStatement = null;
		
		try {
			String sql = "DELETE FROM seller\n" + 
						 "WHERE Id = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if (rowsAffected == 0) {
				throw new DBException("Id not found!");
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(preparedStatement);			
		}
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			String sql = "SELECT seller.*, department.Name as departmentName\n" + 
						 "FROM seller INNER JOIN department\n" + 
						 "ON seller.DepartmentId = department.Id\n" + 
						 "WHERE seller.Id = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, id);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				Department department = instantiateDepartment(resultSet);				
				Seller seller = instantiateSeller(resultSet, department);				
				return seller;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(preparedStatement);
			DBConnection.closeResultset(resultSet);
		}
		
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			String sql = "SELECT seller.*, department.Name as departmentName\n" + 
						 "FROM seller INNER JOIN department\n" + 
						 "ON seller.DepartmentId = department.Id\n" + 						 
						 "ORDER BY Name";
			
			preparedStatement = connection.prepareStatement(sql);
			
			resultSet = preparedStatement.executeQuery();
			
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (resultSet.next()) {
				
				Department returnedDepartment = map.get(resultSet.getInt("departmentId"));
				
				if (returnedDepartment == null) {
					returnedDepartment = instantiateDepartment(resultSet);
					map.put(resultSet.getInt("departmentId"), returnedDepartment);
				}	
							
				sellers.add(instantiateSeller(resultSet, returnedDepartment));
			}
			
			return sellers;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(preparedStatement);
			DBConnection.closeResultset(resultSet);
		}
	}
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			String sql = "SELECT seller.*, department.Name as departmentName\n" + 
						 "FROM seller INNER JOIN department\n" + 
						 "ON seller.DepartmentId = department.Id\n" + 
						 "WHERE DepartmentId = ?\n" + 
						 "ORDER BY Name";
			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, department.getId());
			
			resultSet = preparedStatement.executeQuery();
			
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (resultSet.next()) {
				
				Department returnedDepartment = map.get(resultSet.getInt("departmentId"));
				
				if (returnedDepartment == null) {
					returnedDepartment = instantiateDepartment(resultSet);
					map.put(resultSet.getInt("departmentId"), returnedDepartment);
				}	
							
				sellers.add(instantiateSeller(resultSet, returnedDepartment));
			}
			
			return sellers;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(preparedStatement);
			DBConnection.closeResultset(resultSet);
		}
	}
	
	private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("DepartmentId"));
		department.setName(resultSet.getString("departmentName"));
		return department;
	}
	
	private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(resultSet.getInt("Id"));
		seller.setName(resultSet.getString("Name"));
		seller.setEmail(resultSet.getString("Email"));
		seller.setBirthDate(resultSet.getDate("BirthDate"));
		seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
		seller.setDepartment(department);
		return seller;
	}

}
