package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dbexercise.util.DB2ConnectionManager;

public class Apartment extends Estate{
	private int floor;
	private float rent;
	private int rooms;
	private boolean balcony;
	private boolean builtInKitchen;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public float getRent() {
		return rent;
	}

	public void setRent(float rent) {
		this.rent = rent;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public boolean isBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}

	public boolean isBuiltInKitchen() {
		return builtInKitchen;
	}

	public void setBuiltInKitchen(boolean builtInKitchen) {
		this.builtInKitchen = builtInKitchen;
	}

	/**
	 * Load an Apartment from the database (given the id)
	 * @param id
	 * @return
	 */
	public static Apartment load(int id) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
			// Prepare Statement
			String selectSQL = "SELECT * FROM apartment WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Apartment ap = new Apartment();
				ap.setId(id);
				ap.setFloor(rs.getInt("floor"));
				ap.setRent(rs.getFloat("rent"));
				ap.setRooms(rs.getInt("rooms"));
				ap.setBalcony(rs.getBoolean("balcony"));
				ap.setBuiltInKitchen(rs.getBoolean("builtinkitchen"));
				
				Estate es = Estate.load(id);
				
				ap.setLogin(es.getLogin());
				ap.setCity(es.getCity());
				ap.setPostalCode(es.getPostalCode());
				ap.setStreet(es.getStreet());
				ap.setStreetNumber(es.getStreetNumber());
				ap.setSquareArea(es.getSquareArea());
				
				rs.close();
				pstmt.close();
				return ap;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save an Apartment to the database
	 */
	public void save() {
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			// If it is a new record
			if (getId() == -1) {
				
				//Save the Estate part (and set the ID of the estate as a consequence
				// ..which will be used further on to set the ID field of apartment in the db.)
				super.save();
				
				String insertSQL = "INSERT INTO apartment(id, floor, rent, rooms, balcony, builtinkitchen) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Set parameters of the prepared statements.
				pstmt.setInt(1, getId());
				pstmt.setInt(2, getFloor());
				pstmt.setFloat(3, getRent());
				pstmt.setInt(4, getRooms());
				pstmt.setBoolean(5, isBalcony());
				pstmt.setBoolean(6, isBuiltInKitchen());
				pstmt.executeUpdate();
				pstmt.close();
			} else {
				// If it is an existing record, Update the record.
				
				//Save the Estate part
				super.save();
				
				String updateSQL = "UPDATE apartment SET floor = ?, rent = ?, rooms = ?, balcony = ?, builtinkitchen = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set parameters of the prepared statements.
				pstmt.setInt(1, getFloor());
				pstmt.setFloat(2, getRent());
				pstmt.setInt(3, getRooms());
				pstmt.setBoolean(4, isBalcony());
				pstmt.setBoolean(5, isBuiltInKitchen());
				pstmt.setInt(6, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
