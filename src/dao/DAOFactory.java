package dao;

import db.DBConnection;

public class DAOFactory {

	public static SellerDAO createSellerDAO() {
		return new SellerDAOimpl(DBConnection.getConnection());
	}
	
	public static DepartmentDAO createDepartmentDAO() {
		return new DepartmentoDAOimpl(DBConnection.getConnection());
	}
	
}
