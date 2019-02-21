package com.Jakeob.BigBrother;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLHandler {
	private String host;
	private String port;
	private String user;
	private String password;
	private String database;
	
	private Connection connection;
	
	public SQLHandler(String host, String port, String user, String password, String database) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;
		
		connect();
	}
	
	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
			Statement statement = this.connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS `BBLog` (`Player` VARCHAR(50), `Time` DATETIME, `Reason` VARCHAR(50), `BlockType` VARCHAR(50), `X` INT, `Y` INT, `Z` INT)");
		} catch (Exception e) {
			BigBrother.logger.warning("Couldn't connect to database, make sure Big Brother config is correct!");
		}
	}
	
	public boolean isConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}
}