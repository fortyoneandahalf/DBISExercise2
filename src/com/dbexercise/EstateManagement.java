package com.dbexercise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.dbexercise.data.EstateAgent;
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
		//createTables();
		//createInitialObjects();
		showAllData();
		//new EstateManagement();
		//System.out.println();
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
					//Account creation for estate agent
					EstateAgent.createNew();
					break;
				case 2:
					//Modify Account
					EstateAgent.modify();
					break;
				case 3:
					//Delete Account
					EstateAgent.delete();
					break;
				case 4:
					//Exit
					return;
				default:
					System.out.println("Wrong choice! Try Again!");
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
	
	
	
	
	
	
	
	
	public static void createTables(){
		System.out.println("Hello");
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			con.setAutoCommit(false);
		      System.out.println("**** Created a JDBC connection to the data source");

		      // Create the Statement
		      Statement stmt = con.createStatement();                                            
		      System.out.println("**** Created JDBC Statement object");

		      //URL url = ClassLoader.getSystemResource("createObjects.sql");
		      URL url = ClassLoader.getSystemResource("createTablesSimple.sql");
		      //URL url = ClassLoader.getSystemResource("DropAllTables.sql");
		      
		      String sql= "";
		      Scanner scanIn = null;
		      try {
		    	  scanIn = new Scanner(new File(url.toURI()));
		    	  while(scanIn.hasNext()){
		    		  sql = scanIn.nextLine();
		    		  sql = sql.replace(";", "");
		    		  if(sql.length()>5){
		    			  System.out.println(sql);
		    			  stmt.executeUpdate(sql);
		    			  //con.commit();
		    		  }
		    	  }
		    	  System.out.println(sql);
		      } catch (FileNotFoundException e) {
		    	  e.printStackTrace();
		      } catch (URISyntaxException e) {
		    	  e.printStackTrace();
		      }
		      
		      // Execute a query and generate a ResultSet instance
		      ResultSet rs = stmt.executeQuery("select TABNAME from syscat.tables where tabschema = 'VSISP51'");                    
		      System.out.println("**** Created JDBC ResultSet object");

		      // Print all of the employee numbers to standard output device
		      while (rs.next()) {
		        String col1 = rs.getString(1);
		        System.out.println("Table name = " + col1);
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
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createInitialObjects(){
		System.out.println("Hello");
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			con.setAutoCommit(false);
		      System.out.println("**** Created a JDBC connection to the data source");

		      // Create the Statement
		      Statement stmt = con.createStatement();                                            
		      System.out.println("**** Created JDBC Statement object");

		      URL url = ClassLoader.getSystemResource("createObjects.sql");
		      
		      String sql= "";
		      Scanner scanIn = null;
		      try {
		    	  scanIn = new Scanner(new File(url.toURI()));
		    	  while(scanIn.hasNext()){
		    		  sql = scanIn.nextLine();
		    		  sql = sql.replace(";", "");
		    		  if(sql.length()>5){
		    			  System.out.println(sql);
		    			  stmt.executeUpdate(sql);
		    			  //con.commit();
		    		  }
		    	  }
		    	  System.out.println(sql);
		      } catch (FileNotFoundException e) {
		    	  e.printStackTrace();
		      } catch (URISyntaxException e) {
		    	  e.printStackTrace();
		      }
		      
		      // Execute a query and generate a ResultSet instance
		      ResultSet rs = stmt.executeQuery("select TABNAME from syscat.tables where tabschema = 'VSISP51'");                    
		      System.out.println("**** Created JDBC ResultSet object");

		      // Print all of the employee numbers to standard output device
		      while (rs.next()) {
		        String col1 = rs.getString(1);
		        System.out.println("Table name = " + col1);
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
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void showAllData(){
		System.out.println("Hello");
		try {
			// Get connection
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			
		      //System.out.println("**** Created a JDBC connection to the data source");

		      // Create the Statement
		      Statement stmt = con.createStatement();                                            
		      //System.out.println("**** Created JDBC Statement object");
		      
		      String[] tableNames = {"APARTMENT", "CONTRACT", "ESTATE", "ESTATEAGENT", "HOUSE", "PERSON", "PURCHASECONTRACT", "TENANCYCONTRACT"};
		      
		      for (String tableName : tableNames) {
		    	// Execute a query and generate a ResultSet instance
			      ResultSet rs = stmt.executeQuery("select * from "+tableName);                    
			      //System.out.println("**** Created JDBC ResultSet object");
			      
			      System.out.println("Table "+tableName);
			      // Print all of the data to standard output device
			      int columnCount = rs.getMetaData().getColumnCount();
			      for (int i = 1; i <= columnCount; i++) {
		    		  System.out.print(rs.getMetaData().getColumnName(i));
		    		  if(i<columnCount){
		    			  System.out.print(" - ");
		    		  }
		    	  }
		    	  System.out.println();
			      while (rs.next()) {
			    	  for (int i = 1; i <= columnCount; i++) {
			    		  System.out.print(rs.getString(i));
			    		  if(i<columnCount){
			    			  System.out.print(" - ");
			    		  }
			    	  }
			    	  System.out.println();
			      }
			      //System.out.println("**** Fetched all rows from JDBC ResultSet");
			      // Close the ResultSet
			      rs.close();
			      //System.out.println("**** Closed JDBC ResultSet");
		      }
		      
		      // Close the Statement
		      stmt.close();
		      //System.out.println("**** Closed JDBC Statement");

		      // Connection must be on a unit-of-work boundary to allow close
		      //con.commit();
		      //System.out.println ( "**** Transaction committed" );
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
