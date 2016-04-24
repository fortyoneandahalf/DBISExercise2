package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.dbexercise.util.DB2ConnectionManager;

public class Contract {
	private int contractNo = -1;
	private String date;
	private String place;
	
	public int getContractNo() {
		return contractNo;
	}

	public void setContractNo(int contractNo) {
		this.contractNo = contractNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Override
	public String toString() {
		return "contractNo=" + contractNo + ", date=" + date + ", place=" + place;
	}

	/**
	 * Load a contract from the database (given the contractNo)
	 * @param id
	 * @return
	 */
	public static Contract load(int contractNo) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT * FROM contract WHERE contractno = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNo);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Contract c = new Contract();
				c.setContractNo(contractNo);
				c.setDate(rs.getString("date"));
				c.setPlace(rs.getString("place"));
				
				rs.close();
				pstmt.close();
				DB2ConnectionManager.getInstance().closeConnection();;
				return c;
			}
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save a Contract to the database
	 */
	public boolean save() {
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			// If it is a new record
			if (getContractNo() == -1) {
				String insertSQL = "INSERT INTO contract(date, place) VALUES (?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.executeUpdate();

				// Get the ID from the database and set it to the object!
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setContractNo(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			} else {
				// If it is an existing record, Update the record.
				String updateSQL = "UPDATE contract SET date = ?, place = ? WHERE contractno = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getContractNo());
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
	
	public static boolean delete(int contractNo) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "DELETE * FROM contract WHERE contractno = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNo);

			// Processing result
			pstmt.executeUpdate();
			pstmt.close();
			DB2ConnectionManager.getInstance().closeConnection();;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Contract createNewContract(){
		Contract c = new Contract();
		Scanner scanIn = new Scanner(System.in);
		System.out.print("Enter date: ");
		c.setDate(scanIn.nextLine());
		System.out.print("Enter place: ");
		c.setPlace(scanIn.nextLine());
		return c;
	}
}
