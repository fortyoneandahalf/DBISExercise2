package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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
	
	

	@Override
	public String toString() {
		return "EstateAgent [login=" + login + ", name=" + name + ", address=" + address
//				+ ", password=" + password + "]";
				+ "]";
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
				DB2ConnectionManager.getInstance().closeConnection();
				return ea;
			}
			DB2ConnectionManager.getInstance().closeConnection();;
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
				DB2ConnectionManager.getInstance().closeConnection();;
				return ea;
			}
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save an Estate to the database
	 */
	public boolean save() {
		
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
			DB2ConnectionManager.getInstance().closeConnection();;
			return true;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("YOU DID SOMETHING WRONG!");
			return false;
		}
	}
	
	/**
	 * Delete an EstateAgent from the database
	 */
	public static void delete() {
		System.out.println("Enter the EstateAgent login to delete:");
		String login = (new Scanner(System.in).next());
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			String deleteSQL = "DELETE FROM estateagent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(deleteSQL);

			// Set parameters of the prepared statement.
			pstmt.setString(1, login);
			pstmt.executeUpdate();
			
			pstmt.close();
			con.commit();
			DB2ConnectionManager.getInstance().closeConnection();;
			System.out.println("Sucessfully Deleted EstateAgent: "+login);
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("ERROR Deleting EstateAgent");
		}
		
	}
	
	/**
	 * Create New EstateAgent -> the request for input dialogues/whatever it is called.
	 * @return
	 */
	public static void createNew(){
		Scanner scanIn = new Scanner(System.in);
		EstateAgent ea = new EstateAgent();
		ea.setNewRecord(true);
		System.out.print("Enter a login:");
		ea.setLogin(scanIn.nextLine());
		System.out.print("Enter your name:");
		ea.setName(scanIn.nextLine());
		System.out.print("Enter your address:");
		ea.setAddress(scanIn.nextLine());
		System.out.print("Enter a password:");
		ea.setPassword(scanIn.nextLine());
		System.out.println("");
		if(ea.save()){
			System.out.println("Sucessfully Created New EstateAgent");
			System.out.println(ea);
		}else{
			System.out.println("ERROR CREATING New EstateAgent");
		}
	}
	
	public static void modify(){
		Scanner scanIn = new Scanner(System.in);
		System.out.println("Enter the login for modification:");
		EstateAgent ea = EstateAgent.load(scanIn.nextLine());
		System.out.println("Current details:"+ea);
		if(ea!=null){
//			System.out.println("Enter your new login:");
//			ea.setLogin(scanIn.nextLine());
			System.out.print("Enter the new name:");
			ea.setName(scanIn.nextLine());
			System.out.print("Enter the new address:");
			ea.setAddress(scanIn.nextLine());
			System.out.print("Enter the new password:");
			ea.setPassword(scanIn.nextLine());
			System.out.println("");
			if(ea.save()){
				System.out.println("Sucessfully Modified EstateAgent");
				System.out.println("New details:"+ea);
				return;
			}
		}
		System.out.println("ERROR MODIFYING New EstateAgent");
	}
	
}
