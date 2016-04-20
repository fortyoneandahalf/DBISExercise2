package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dbexercise.util.DB2ConnectionManager;

public class PurchaseContract extends Contract {
	
	private int noOfInstallments;
	private float interestRate;
	private House house;
	private Person person;

	public int getNoOfInstallments() {
		return noOfInstallments;
	}

	public void setNoOfInstallments(int noOfInstallments) {
		this.noOfInstallments = noOfInstallments;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * Load a Purchase Contract from the database (given the contractNo)
	 * @param id
	 * @return
	 */
	public static PurchaseContract load(int contractNo) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT * FROM purchasecontract WHERE contractno = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNo);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				PurchaseContract pc = new PurchaseContract();
				pc.setContractNo(contractNo);
				pc.setNoOfInstallments(rs.getInt("noofinstallments"));
				pc.setInterestRate(rs.getFloat("interestrate"));
				pc.setHouse(House.load(rs.getInt("houseid")));
				pc.setPerson(Person.load(rs.getInt("personid")));
				
				Contract c = Contract.load(contractNo);
				pc.setDate(c.getDate());
				pc.setPlace(c.getPlace());
				
				rs.close();
				pstmt.close();
				return pc;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save a Purchase Contract to the database
	 */
	public void save() {
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			// If it is a new record
			if (getContractNo() == -1) {
				
				//Save the Contract part (and set the ID of the contract as a consequence
				// ..which will be used further on to set the ID field of purchase contract in the db.)
				super.save();
				
				String insertSQL = "INSERT INTO purchasecontract(contractno, noofinstallments, interestrate, houseid, personid) VALUES (?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Set parameters of the prepared statements.
				pstmt.setInt(1, getContractNo());
				pstmt.setInt(2, getNoOfInstallments());
				pstmt.setFloat(3, getInterestRate());
				pstmt.setInt(4, getHouse().getId());
				pstmt.setInt(5, getPerson().getId());
				pstmt.executeUpdate();
				pstmt.close();
			} else {
				// If it is an existing record, Update the record.
				
				//Save the Contract part
				super.save();
				
				String updateSQL = "UPDATE purchasecontract SET noofinstallments = ?, interestrate = ?, houseid = ?, personid = ? WHERE contractno = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set parameters of the prepared statements.
				pstmt.setInt(1, getNoOfInstallments());
				pstmt.setFloat(2, getInterestRate());
				pstmt.setInt(3, getHouse().getId());
				pstmt.setInt(4, getPerson().getId());
				pstmt.setInt(5, getContractNo());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
