package com.Jakeob.BigBrother;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
	
	private SQLSender sqls;
	private Connection connection;
	
	public SQLHandler(String host, String port, String user, String password, String database) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;
		
		connect();
		sqls = new SQLSender(this.connection);
		Thread queryThread = new Thread(sqls);
		queryThread.start();
	}
	
	private void connect() {
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
			Statement statement = this.connection.createStatement();
			String createTable = "CREATE TABLE IF NOT EXISTS `BBLog` (`Player` VARCHAR(50), `Time` DATETIME, `Event` VARCHAR(50), `BlockType` VARCHAR(50), `World` VARCHAR(50), `X` INT, `Y` INT, `Z` INT)";
			statement.execute(createTable);
			BigBrother.logger.info("Connected to database!");
		} catch (Exception e) {
			BigBrother.logger.warning("Couldn't connect to database, make sure Big Brother config is correct!");
		}
	}
	
	public void addEntry(String entry) {
		sqls.addQuery(entry);
	}
	
	public static String getInsertStatement(String playerName, String event, Material type, Location loc) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String time = sdf.format(date);
		String blockType = type.toString();
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		String values = "'" + playerName + "', '" + time + "', '" + event + "', '" + blockType + "', '" + world + "', '" + x + "', '" + y + "', '" + z + "'";
		
		return "INSERT INTO `BBLog` (`Player`, `Time`, `Event`, `BlockType`, `World`, `X`, `Y`, `Z`) VALUES (" + values + ")";
	}
	
	public static String getInsertStatement(String playerName, String time, String event, String blockType, String world, int x, int y, int z) {
		String values = "'" + playerName + "', '" + time + "', '" + event + "', '" + blockType + "', '" + world + "', '" + x + "', '" + y + "', '" + z + "'";;
		return "INSERT INTO `BBLog` (`Player`, `Time`, `Event`, `BlockType`, `World`, `X`, `Y`, `Z`) VALUES (" + values + ")";
	}
	
	public ResultSet getResultSet(String world, int x, int y, int z) {
		ResultSet resultSet = null;
		
		try {
			Statement statement = this.connection.createStatement();
			String selectStatement = "SELECT * FROM `BBLog` WHERE World = '" + world + "' AND X = " + x + " AND Y = " + y + " AND Z = " + z + ";";
			resultSet = statement.executeQuery(selectStatement);
		}catch (Exception exception) {
			BigBrother.logger.warning("Problem grabbing result set!");
		}
		
		return resultSet;
	}
	
	public ResultSet getResultSet(String conditions) {
		ResultSet resultSet = null;
		
		try {
			Statement statement = this.connection.createStatement();
			String selectStatement = "SELECT * FROM `BBLog` WHERE " + conditions +  ";";
			resultSet = statement.executeQuery(selectStatement);
			BigBrother.logger.info(selectStatement);
		}catch (Exception exception) {
			BigBrother.logger.warning("Problem grabbing result set!");
		}
		
		return resultSet;
	}
	
	public boolean isConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}
}