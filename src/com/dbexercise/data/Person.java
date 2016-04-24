package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.dbexercise.util.DB2ConnectionManager;

public class Person {
	private int id = -1;
	private String firstName;
	private String name;
	private String address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	
	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", name=" + name + ", address=" + address + "]";
	}

	/**
	 * Load a person from the database (given the id)
	 * @param id
	 * @return
	 */
	public static Person load(int id) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT * FROM person WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Person p = new Person();
				p.setId(id);
				p.setFirstName(rs.getString("firstname"));
				p.setName(rs.getString("name"));
				p.setAddress(rs.getString("address"));
				
				rs.close();
				pstmt.close();
				DB2ConnectionManager.getInstance().closeConnection();;
				return p;
			}
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save a Person to the database
	 */
	public boolean save() {
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			// If it is a new record
			if (getId() == -1) {
				String insertSQL = "INSERT INTO person(firstname, name, address) VALUES (?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getFirstName());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.executeUpdate();

				// Get the ID from the database and set it to the object!
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			} else {
				// If it is an existing record, Update the record.
				String updateSQL = "UPDATE person SET firstname = ?, name = ?, address = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getFirstName());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.setInt(4, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
			con.commit();
			DB2ConnectionManager.getInstance().closeConnection();;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void createNewPerson(){
		Person p = new Person();
		Scanner scanIn = new Scanner(System.in);
		
		System.out.print("Enter First Name: ");
		p.setFirstName(scanIn.nextLine());
		System.out.print("Enter Name: ");
		p.setName(scanIn.nextLine());
		System.out.print("Enter Address: ");
		p.setAddress(scanIn.nextLine());
		
		
		if(p.save()){
			System.out.println("Sucessfully Created New Person");
			System.out.println(p);
		}else{
			System.out.println("ERROR CREATING New Person");
		}
		
	}
	
	public static void listAllPersons(){
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
			// Prepare Statement
			String selectSQL = "SELECT * FROM person";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Person p = new Person();
				p.setId(rs.getInt("id"));
				p.setFirstName(rs.getString("firstname"));
				p.setName(rs.getString("name"));
				p.setAddress(rs.getString("address"));
				
				System.out.println(p);
			}
			rs.close();
			pstmt.close();
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isValidPerson(int id){
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
			// Prepare Statement
			String selectSQL = "SELECT * FROM person where id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				rs.close();
				pstmt.close();
				DB2ConnectionManager.getInstance().closeConnection();;
				return true;
			}
			
			rs.close();
			pstmt.close();
			DB2ConnectionManager.getInstance().closeConnection();;
			return false;
			
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("ERROR: Query cannot be executed!");
			return false;
		}
	}
	
}
