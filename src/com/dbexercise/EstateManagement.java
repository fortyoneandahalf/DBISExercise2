package com.dbexercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.dbexercise.util.DB2ConnectionManager;

public class EstateManagement {
	
	private static final String [] MAIN_MENU_ITEMS = {
			"MAIN MENU",
			"1. Management mode for estate agents",
			"2. Management mode for estates",
			"3. Contract management",
			"4. Exit"};
	
	private static final String [] MENU_ESTATE_AGENTS_ITEMS = {
			"MANAGEMENT MODE FOR ESTATE AGENTS",
			"1. Account Creation",
            "2. Modify Account",
            "3. Delete Account",
            "4. Back to Main Menu"};
	
	private static final String [] MENU_ESTATES_LEVEL_1_ITEMS = {
			"MANAGEMENT MODE FOR ESTATES",
			"1. Log In",
            "2. Back to Main Menu"};
	
	private static final String [] MENU_ESTATES_LEVEL_2_ITEMS = {
			"MANAGEMENT MODE FOR ESTATES - Logged In",
			"1. Create Estate",
			"2. Modify Estate",
			"3. Update Estate",
            "4. Log out and return to Main Menu"};
	
	private static final String [] MENU_ESTATES_LEVEL_3_ITEMS = {
			"MANAGEMENT MODE FOR ESTATES - Choose Property type",
			"1. Apartment",
			"2. House",
            "3. Back to MANAGEMENT MODE FOR ESTATES"};
	
	private static final String [] MENU_CONTRACT_LEVEL_1_ITEMS = {
			"CONTRACT MANAGEMENT",
			"1. Insert Persons",
			"2. Sign (Create) Contracts",
			"3. Overview of all Contracts",
            "4. Back to Main Menu"};
	
	private static final String [] MENU_CONTRACT_LEVEL_2_ITEMS = {
			"CONTRACT MANAGEMENT - Choose Contract Type",
			"1. Purchase Contract (for House)",
			"2. Tenancy Contract (for Apartment)",
			"3. Back to CONTRACT MANAGEMENT"};
	
	

	public static void main(String[] args) {
		fn1();
		//new EstateManagement();
	}
	
	public EstateManagement(){
		while(true){
			int choice = displayMenu(EstateManagement.MAIN_MENU_ITEMS);
			switch (choice) {
				case 1:
					estateAgentManagementMode();
					break;
				case 2:
					estateManagementMode();
					break;
				case 3:
					contractManagementMode();
					break;
				case 4:
					//Exit
					break;
				default:
					System.out.println("Wrong choice! Try Again!");
					break;
			}
			if (choice == 4){
				break;
			}
		}
	}
	
	private int displayMenu(String[] menuItems){
		for (String menuItem : menuItems) {
			System.out.println(menuItem);
		}
		
		System.out.println("Enter your choice:");
		
		try{
			return (new Scanner(System.in)).nextInt();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private void estateAgentManagementMode(){
		//1. Management mode for estate agents
		while(true){
			int choice = displayMenu(EstateManagement.MENU_ESTATE_AGENTS_ITEMS);
			switch (choice) {
				case 1:
					estateAgentManagementMode();
					break;
				case 2:
					estateManagementMode();
					break;
				case 3:
					contractManagementMode();
					break;
				case 4:
					//Exit
					break;
				default:
					System.out.println("Wrong choice! Try Again!");
					break;
			}
			if (choice == 4){
				break;
			}
		}
	}
	
	private void estateManagementMode(){
		//2. Management mode for estates
		
	}
	
	private void contractManagementMode(){
		//3. Contract management
		
	}
	
	
	
	
	
	
	
	
	public static void fn1(){
		System.out.println("Hello");
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			con.setAutoCommit(false);
		      System.out.println("**** Created a JDBC connection to the data source");

		      // Create the Statement
		      Statement stmt = con.createStatement();                                            
		      System.out.println("**** Created JDBC Statement object");

		      // Execute a query and generate a ResultSet instance
		      ResultSet rs = stmt.executeQuery("SELECT a FROM tbl2");                    
		      System.out.println("**** Created JDBC ResultSet object");

		      // Print all of the employee numbers to standard output device
		      while (rs.next()) {
		        String empNo = rs.getString(1);
		        System.out.println("Employee number = " + empNo);
		      }
		      System.out.println("**** Fetched all rows from JDBC ResultSet");
		      // Close the ResultSet
		      rs.close();
		      System.out.println("**** Closed JDBC ResultSet");
		      
		      // Close the Statement
		      stmt.close();
		      System.out.println("**** Closed JDBC Statement");

		      // Connection must be on a unit-of-work boundary to allow close
		      con.commit();
		      System.out.println ( "**** Transaction committed" );
		      
		      
//			System.out.println("Hello2");
//			// Prepare Statement
//			String selectSQL = "db2 LIST TABLES FOR ALL;";
//			PreparedStatement pstmt = con.prepareStatement(selectSQL);
////			ResultSet rss = con.createStatement().executeQuery("db2 LIST TABLES FOR ALL;");
////			rss.next();
////			System.out.println(rss.getString(1));
//			//pstmt.setInt(1, id);
//
//			// Processing result
//			pstmt.executeUpdate();
//			pstmt.exe
//			ResultSet rs = pstmt.executeQuery();
//			System.out.println("ss");
//			if (rs.next()) {
//					
//				rs.close();
//				pstmt.close();
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
