package com.dbexercise.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DB2ConnectionManager {

	// instance of DB2ConnectionManager
	private static DB2ConnectionManager instance = null;

	// DB2 connection
	private Connection conn;
	
	private final String JDBC_USER = "vsisp51";
	private final String JDBC_PASS = "hXol4JXs";
	private final String JDBC_URL = "jdbc:db2://vsisls4.informatik.uni-hamburg.de:50001/VSISP";

	/**
	 * Constructor
	 */
	private DB2ConnectionManager() {
		try {
			// Loads the driver.
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get instance of the DB2ConnectionManager (Enforces Singleton Pattern)
	 * 
	 * @return DB2ConnectionManager
	 */
	public static DB2ConnectionManager getInstance() {
		if (instance == null) {
			instance = new DB2ConnectionManager();
		}
		return instance;
	}

	/**
	 * Get the DB Connection.
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		return conn;
	}

}