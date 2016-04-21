package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dbexercise.util.DB2ConnectionManager;

public class EstateAgent {
	private boolean newRecord = true;
	private String login;
	private String name;
	private String address;
	private String password;

	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Load an estate agent from the database (given the login)
	 * @param loadLogin
	 * @param loadPassword
	 * @return
	 */
	public static EstateAgent load(String loadLogin) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT * FROM estateagent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, loadLogin);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				EstateAgent ea = new EstateAgent();
				ea.setNewRecord(false);
				ea.setLogin(loadLogin);
				ea.setName(rs.getString("name"));
				ea.setAddress(rs.getString("address"));
				ea.setPassword(rs.getString("password"));
				rs.close();
				pstmt.close();
				return ea;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Load an estate agent from the database (given the login and password)
	 * @param loadLogin
	 * @param loadPassword
	 * @return
	 */
	public static EstateAgent authenticatedLoad(String loadLogin, String loadPassword) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT * FROM estateagent WHERE login = ? AND password = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, loadLogin);
			pstmt.setString(2, loadPassword);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				EstateAgent ea = new EstateAgent();
				ea.setNewRecord(false);
				ea.setLogin(loadLogin);
				ea.setName(rs.getString("name"));
				ea.setAddress(rs.getString("address"));
				ea.setPassword(loadPassword);
				rs.close();
				pstmt.close();
				return ea;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save an Estate to the database
	 */
	public void save() {
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			// If it is a new record
			if (isNewRecord()) {
				String insertSQL = "INSERT INTO estateagent (login, name, address, password) VALUES (?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getLogin());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.setString(4, getPassword());
				pstmt.executeUpdate();

				// Ensure that the Object is no longer a new Record!
				setNewRecord(false);

				pstmt.close();
			} else {
				// If it is an existing record, Update the record.
				String updateSQL = "UPDATE estateagent SET name = ?, address = ?, password = ? WHERE login = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getPassword());
				pstmt.setString(4, getLogin());
				pstmt.executeUpdate();

				pstmt.close();
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
