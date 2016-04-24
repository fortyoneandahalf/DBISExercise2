package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dbexercise.util.DB2ConnectionManager;

public class TenancyContract extends Contract {
	
	private String startDate;
	private int duration;
	private float additionalCosts;
	private Apartment apartment;
	private Person person;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public float getAdditionalCosts() {
		return additionalCosts;
	}

	public void setAdditionalCosts(float additionalCosts) {
		this.additionalCosts = additionalCosts;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	@Override
	public String toString() {
		return "TenancyContract ["+super.toString()+", startDate=" + startDate + ", duration=" + duration + ", additionalCosts="
				+ additionalCosts + ", apartmentId=" + apartment.getId() + ", personId=" + person.getId() + "]";
	}

	/**
	 * Load a Tenancy Contract from the database (given the contractNo)
	 * @param id
	 * @return
	 */
	public static TenancyContract load(int contractNo) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT * FROM tenancycontract WHERE contractno = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNo);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				TenancyContract tc = new TenancyContract();
				tc.setContractNo(contractNo);
				tc.setStartDate(rs.getString("startdate"));
				tc.setDuration(rs.getInt("duration"));
				tc.setAdditionalCosts(rs.getFloat("additionalcosts"));
				tc.setApartment(Apartment.load(rs.getInt("apartmentid")));
				tc.setPerson(Person.load(rs.getInt("personid")));
				
				Contract c = Contract.load(contractNo);
				tc.setDate(c.getDate());
				tc.setPlace(c.getPlace());
				
				rs.close();
				pstmt.close();
				DB2ConnectionManager.getInstance().closeConnection();;
				return tc;
			}
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save a Tenancy Contract to the database
	 */
	public boolean save() {
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			// If it is a new record
			if (getContractNo() == -1) {
				
				//Save the Contract part (and set the ID of the contract as a consequence
				// ..which will be used further on to set the ID field of tenancy contract in the db.)
				super.save();
				
				String insertSQL = "INSERT INTO tenancycontract(contractno, startdate, duration, additionalcosts, apartmentid, personid) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Set parameters of the prepared statements.
				pstmt.setInt(1, getContractNo());
				pstmt.setString(2, getStartDate());
				pstmt.setInt(3, getDuration());
				pstmt.setFloat(4, getAdditionalCosts());
				pstmt.setInt(5, getApartment().getId());
				pstmt.setInt(6, getPerson().getId());
				pstmt.executeUpdate();
				pstmt.close();
			} else {
				// If it is an existing record, Update the record.
				
				//Save the Contract part
				super.save();
				
				String updateSQL = "UPDATE tenancycontract SET startdate = ?, duration = ?, additionalcosts = ?, apartmentid = ?, personid = ? WHERE contractno = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set parameters of the prepared statements.
				pstmt.setString(1, getStartDate());
				pstmt.setInt(2, getDuration());
				pstmt.setFloat(3, getAdditionalCosts());
				pstmt.setInt(4, getApartment().getId());
				pstmt.setInt(5, getPerson().getId());
				pstmt.setInt(6, getContractNo());
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
	
	public static int loadContractNoUsingApartmentId(int id) {
		int contractNo = -1;
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Prepare Statement
			String selectSQL = "SELECT contractno FROM tenancycontract WHERE apartmentid = ?";
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
			System.out.println("ERROR loading ContractNo Using Apartment ID");
		}
		return contractNo;
	}
	
	public static void createNewTenancyContract(Apartment a, Person p){
		TenancyContract tc = new TenancyContract();
		tc.setApartment(a);
		tc.setPerson(p);
		
		Contract c = Contract.createNewContract();
		tc.setDate(c.getDate());
		tc.setPlace(c.getPlace());
		
		Scanner scanIn = new Scanner(System.in);
		System.out.print("Enter the start date: ");
		tc.setStartDate(scanIn.nextLine());
		System.out.print("Enter the duration (int): ");
		tc.setDuration(scanIn.nextInt());
		System.out.print("Enter the Additional costs: ");
		tc.setAdditionalCosts(scanIn.nextFloat());
		
		if(tc.save()){
			System.out.println("Sucessfully Created Tenancy Contract");
			System.out.println(tc);
		}else{
			System.out.println("ERROR CREATING Tenancy Contract");
		}
	}
	
	public static void listAllTenancyContracts(){
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
			// Prepare Statement
			String selectSQL = "SELECT * FROM tenancycontract";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println(TenancyContract.load(rs.getInt("contractno")));
			}
			rs.close();
			pstmt.close();
			DB2ConnectionManager.getInstance().closeConnection();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
