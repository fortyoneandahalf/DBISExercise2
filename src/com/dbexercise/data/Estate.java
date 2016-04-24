package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.dbexercise.util.DB2ConnectionManager;

public class Estate {
	private int id = -1;
	private EstateAgent estateAgent;
	private String city;
	private String postalCode;
	private String street;
	private String streetNumber;
	private float squareArea;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public EstateAgent getEstateAgent() {
		return estateAgent;
	}

	public void setEstateAgent(EstateAgent estateAgent) {
		this.estateAgent = estateAgent;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public float getSquareArea() {
		return squareArea;
	}

	public void setSquareArea(float squareArea) {
		this.squareArea = squareArea;
	}
	
	

	@Override
	public String toString() {
		return " id=" + id + ", estateAgent=" + estateAgent.getLogin() + ", city=" + city + ", postalCode=" + postalCode
				+ ", street=" + street + ", streetNumber=" + streetNumber + ", squareArea=" + squareArea + ", ";
	}

	/**
	 * Load an estate from the database (given the id)
	 * @param id
	 * @return
	 */
	public static Estate load(int id) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT * FROM estate WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Estate es = new Estate();
				es.setId(id);
				es.setEstateAgent(EstateAgent.load(rs.getString("login")));
				es.setCity(rs.getString("city"));
				es.setPostalCode(rs.getString("postalcode"));
				es.setStreet(rs.getString("street"));
				es.setStreetNumber(rs.getString("streetnumber"));
				es.setSquareArea(rs.getFloat("squarearea"));
				
				rs.close();
				pstmt.close();
				return es;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save an Estate to the database
	 * @return 
	 */
	public boolean save() {
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			// If it is a new record
			if (getId() == -1) {
				String insertSQL = "INSERT INTO estate(login, city, postalcode, street, streetnumber, squarearea) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getEstateAgent().getLogin());
				pstmt.setString(2, getCity());
				pstmt.setString(3, getPostalCode());
				pstmt.setString(4, getStreet());
				pstmt.setString(5, getStreetNumber());
				pstmt.setFloat(6, getSquareArea());
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
				String updateSQL = "UPDATE estate SET login = ?, city = ?, postalcode = ?, street = ?, streetnumber = ?, squarearea = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getEstateAgent().getLogin());
				pstmt.setString(2, getCity());
				pstmt.setString(3, getPostalCode());
				pstmt.setString(4, getStreet());
				pstmt.setString(5, getStreetNumber());
				pstmt.setFloat(6, getSquareArea());
				pstmt.setInt(7, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
			con.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Estate createNewEstate(EstateAgent ea){
		Estate es = new Estate();
		Scanner scanIn = new Scanner(System.in);
		es.setEstateAgent(ea);
		System.out.print("Enter City: ");
		es.setCity(scanIn.nextLine());
		System.out.print("Enter Postal Code: ");
		es.setPostalCode(scanIn.nextLine());
		System.out.print("Enter Street: ");
		es.setStreet(scanIn.nextLine());
		System.out.print("Enter Street Number: ");
		es.setStreetNumber(scanIn.nextLine());
		System.out.print("Enter Square Area: ");
		es.setSquareArea(scanIn.nextFloat());
		return es;
	}
	
	public static void modifyEstate(Estate es){
		Scanner scanIn = new Scanner(System.in);
		System.out.print("Enter New City: ");
		es.setCity(scanIn.nextLine());
		System.out.print("Enter New Postal Code: ");
		es.setPostalCode(scanIn.nextLine());
		System.out.print("Enter New Street: ");
		es.setStreet(scanIn.nextLine());
		System.out.print("Enter New Street Number: ");
		es.setStreetNumber(scanIn.nextLine());
		System.out.print("Enter New Square Area: ");
		es.setSquareArea(scanIn.nextFloat());
	}
	
	public static boolean delete(int id) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "DELETE * FROM FROM estate WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Processing result
			pstmt.executeUpdate();
			pstmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
