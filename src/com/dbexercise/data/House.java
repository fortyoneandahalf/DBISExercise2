package com.dbexercise.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dbexercise.util.DB2ConnectionManager;

public class House extends Estate{
	private int floors;
	private float price;
	private boolean garden;

	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}

	@Override
	public String toString() {
		return "House [" + super.toString() + "floor=" + floors + ", price=" + price + ", garden=" + garden + "]";
	}
	
	/**
	 * Load a House from the database (given the id)
	 * @param id
	 * @return
	 */
	public static House load(int id) {
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
			// Prepare Statement
			String selectSQL = "SELECT * FROM house WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				House hou = new House();
				hou.setId(id);
				hou.setFloors(rs.getInt("floors"));
				hou.setPrice(rs.getFloat("price"));
				hou.setGarden(rs.getInt("garden") != 0);
				
				Estate es = Estate.load(id);
				
				hou.setEstateAgent(es.getEstateAgent());
				hou.setCity(es.getCity());
				hou.setPostalCode(es.getPostalCode());
				hou.setStreet(es.getStreet());
				hou.setStreetNumber(es.getStreetNumber());
				hou.setSquareArea(es.getSquareArea());
				
				rs.close();
				pstmt.close();
				return hou;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void listValidHouses(EstateAgent ea){
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
			// Prepare Statement
			String selectSQL = "SELECT * FROM house, estate WHERE house.id = estate.id AND estate.login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, ea.getLogin());

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				House hou = new House();
				hou.setId(rs.getInt("id"));
				hou.setFloors(rs.getInt("floors"));
				hou.setPrice(rs.getFloat("price"));
				hou.setGarden(rs.getInt("garden") != 0);
				
				Estate es = Estate.load(hou.getId());
				
				hou.setEstateAgent(es.getEstateAgent());
				hou.setCity(es.getCity());
				hou.setPostalCode(es.getPostalCode());
				hou.setStreet(es.getStreet());
				hou.setStreetNumber(es.getStreetNumber());
				hou.setSquareArea(es.getSquareArea());
				
				System.out.println(hou);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isValidApartment(EstateAgent ea, int estateId){
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
			// Prepare Statement
			String selectSQL = "SELECT * FROM house, estate WHERE house.id = estate.id AND estate.login = ? AND estate.id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, ea.getLogin());
			pstmt.setInt(2, estateId);

			// Processing result
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				rs.close();
				pstmt.close();
				return true;
			}
			
			rs.close();
			pstmt.close();
			return false;
			
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("ERROR: Query cannot be executed!");
			return false;
		}
	}
	
	/**
	 * Save a House to the database
	 */
	public boolean save() {
		
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
						
			// If it is a new record
			if (getId() == -1) {
				
				//Save the Estate part (and set the ID of the estate as a consequence
				// ..which will be used further on to set the ID field of house in the db.)
				super.save();
				
				String insertSQL = "INSERT INTO house(id, floors, price, garden) VALUES (?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Set parameters of the prepared statements.
				pstmt.setInt(1, getId());
				pstmt.setInt(2, getFloors());
				pstmt.setFloat(3, getPrice());
				pstmt.setInt(4, isGarden()? 1 : 0);
				pstmt.executeUpdate();
				pstmt.close();
			} else {
				// If it is an existing record, Update the record.
				
				//Save the Estate part
				super.save();
				
				String updateSQL = "UPDATE house SET floors = ?, price = ?, garden = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set parameters of the prepared statements.
				pstmt.setInt(1, getFloors());
				pstmt.setFloat(2, getPrice());
				pstmt.setInt(3, isGarden()? 1 : 0);
				pstmt.setInt(4, getId());
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
	
	public static void createNewHouse(EstateAgent ea){
		House hou = new House();
		Estate es = Estate.createNewEstate(ea);
		
		hou.setEstateAgent(es.getEstateAgent());
		hou.setCity(es.getCity());
		hou.setPostalCode(es.getPostalCode());
		hou.setStreet(es.getStreet());
		hou.setStreetNumber(es.getStreetNumber());
		hou.setSquareArea(es.getSquareArea());
		
		Scanner scanIn = new Scanner(System.in);
		System.out.print("Enter Number of Floors: ");
		hou.setFloors(scanIn.nextInt());
		System.out.print("Enter Price: ");
		hou.setPrice(scanIn.nextFloat());
		System.out.print("Enter garden (True/False): ");
		hou.setGarden(scanIn.nextBoolean());
		
		
		if(hou.save()){
			System.out.println("Sucessfully Created New House");
			System.out.println(hou);
		}else{
			System.out.println("ERROR CREATING New House");
		}
		
	}
	
	public static void modifyHouse(int estateId){
		//DisplayApartment
		House hou = House.load(estateId);
		System.out.println(hou);
		Estate.modifyEstate(hou);
		
		Scanner scanIn = new Scanner(System.in);
		System.out.print("Enter New Number of Floors: ");
		hou.setFloors(scanIn.nextInt());
		System.out.print("Enter New Price: ");
		hou.setPrice(scanIn.nextFloat());
		System.out.print("Enter New garden (True/False): ");
		hou.setGarden(scanIn.nextBoolean());
		
		if(hou.save()){
			System.out.println("Sucessfully Modified House");
			System.out.println(hou);
		}else{
			System.out.println("ERROR MODIFYING House");
		}
	}
	
	public static boolean delete(int id){
		//FIND CONTRACT
		int contractNo = PurchaseContract.loadContractNoUsingHouseId(id);
		if(contractNo != -1){
			//CONTRACT FOUND!
			//DELETE CONTRACT (Subsequently it deletes the record in the tenancyContract inherited table)
			Contract.delete(contractNo);
		}
		//DELETE ESTATE (Subsequently it deletes the record in the Apartment inherited table)
		return Estate.delete(id);
		
	}
	
}
