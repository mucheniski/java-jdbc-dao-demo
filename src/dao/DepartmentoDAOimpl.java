package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import db.DBException;
import entities.Department;

public class DepartmentoDAOimpl implements DepartmentDAO {
	
	private Connection connection;
	
	public DepartmentoDAOimpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department department) {
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String sql = "INSERT INTO department\n" + 
					 "(Name)\n" + 
					 "VALUES\n" + 
					 "(?)";
			
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, department.getName());
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(preparedStatement);
		}
	}

	@Override
	public void update(Department department) {
		PreparedStatement preparedStatement = null;
		
		try {
			
			String sql = "UPDATE department\n" + 
						 "SET Name = ?\n" + 
						 "WHERE Id = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, department.getName());
			
			preparedStatement.setInt(2, department.getId());
			
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
			String sql = "DELETE FROM department\n" + 
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
	public Department findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			String sql = "SELECT * FROM department WHERE Id = ?";			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, id);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				Department department = instantiateDepartment(resultSet);	
				return department;
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
	public List<Department> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			String sql = "SELECT * FROM department order by name";			
			preparedStatement = connection.prepareStatement(sql);			
			resultSet = preparedStatement.executeQuery();			
			List<Department> departments = new ArrayList<>();			
			
			while (resultSet.next()) {				
				departments.add(instantiateDepartment(resultSet));
			}
			
			return departments;
			
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
		department.setId(resultSet.getInt("Id"));
		department.setName(resultSet.getString("Name"));
		return department;
	}

}
