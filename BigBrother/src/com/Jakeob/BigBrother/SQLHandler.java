package com.Jakeob.BigBrother;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Material;

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
			this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
			Statement statement = this.connection.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS `BBLog` (`Player` VARCHAR(50), `Time` DATETIME, `BlockType` VARCHAR(50), `World` VARCHAR(50), `X` INT, `Y` INT, `Z` INT)";
			statement.execute(createTable);
		} catch (Exception e) {
			BigBrother.logger.warning("Couldn't connect to database, make sure Big Brother config is correct!");
		}
	}
	
	public void addEntry(String playerName, Material type, Location loc) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String time = sdf.format(date);
		String blockType = type.toString();
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		String values = "'" + playerName + "', '" + time + "', '" + blockType + "', '" + world + "', '" + x + "', '" + y + "', '" + z + "'";
		
		try {
			Statement statement = this.connection.createStatement();
			String entry = "INSERT INTO `BBLog` (`Player`, `Time`, `BlockType`, `World`, `X`, `Y`, `Z`) VALUES (" + values + ")";
			statement.execute(entry);
		} catch (SQLException e) {
			BigBrother.logger.warning("Encountered problem adding entry to server!");
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