package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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
	
	

	@Override
	public String toString() {
		return "PurchaseContract ["+super.toString()+", noOfInstallments=" + noOfInstallments + ", interestRate=" + interestRate + ", houseId="
				+ house.getId() + ", personId=" + person.getId() + "]";
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
				DB2ConnectionManager.getInstance().closeConnection();;
				return pc;
			}
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save a Purchase Contract to the database
	 */
	public boolean save() {
		
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
			con.commit();
			DB2ConnectionManager.getInstance().closeConnection();;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static int loadContractNoUsingHouseId(int id) {
		int contractNo = -1;
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT contractno FROM purchasecontract WHERE houseid = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				contractNo = rs.getInt("contractno");
			}
			rs.close();
			pstmt.close();
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERROR loading ContractNo Using House ID");
		}
		return contractNo;
	}
	
	public static void createNewPurchaseContract(House h, Person p){
		PurchaseContract pc = new PurchaseContract();
		pc.setHouse(h);
		pc.setPerson(p);
		
		Contract c = Contract.createNewContract();
		pc.setDate(c.getDate());
		pc.setPlace(c.getPlace());
		
		Scanner scanIn = new Scanner(System.in);
		System.out.print("Enter the number of installments: ");
		pc.setNoOfInstallments(scanIn.nextInt());
		System.out.print("Enter the interest rate (Float): ");
		pc.setInterestRate(scanIn.nextFloat());
		
		if(pc.save()){
			System.out.println("Sucessfully Created Purchase Contract");
			System.out.println(pc);
		}else{
			System.out.println("ERROR CREATING Purchase Contract");
		}
	}
	
	public static void listAllPurchaseContracts(){
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
			// Prepare Statement
			String selectSQL = "SELECT * FROM purchasecontract";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println(PurchaseContract.load(rs.getInt("contractno")));
			}
			rs.close();
			pstmt.close();
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
