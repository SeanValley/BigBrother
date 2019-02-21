package com.Jakeob.BigBrother;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Big Brother by Sean Valley
 * Email: valleydsean@gmail.com
 * Github: github.com/SeanValley
 * 
 * TODO:
 * Create BlockListener and allow it to log changes
 * Don't forget to add BlockExplodeEvent
 * 
 * Initialize CommandParser
 * Create following commands:
 *   bb - shows player help for Big Brother
 *   bb help - shows player help for Big Brother
 *   bb log - gives player log tool or removes it if they already have it
 *   bb history - gives player block change history based on parameters supplied
 *   bb rollback - rolls back changes based on parameters supplied by player
 *   bb undo - undoes roll backs made by player
 *   
 * Create permissions for various commands
 * 
 * Allow plugin to hook into WorldEdit for extra control
 */

public class BigBrother extends JavaPlugin{
	public static Logger logger = null;
	
	public void onEnable() {
		logger = this.getServer().getLogger();
		
		loadConfiguration();

		String host = this.getConfig().getString("MySQL.Host");
		String port = this.getConfig().getString("MySQL.Port");
		String user = this.getConfig().getString("MySQL.User");
		String password = this.getConfig().getString("MySQL.Password");
		String database = this.getConfig().getString("MySQL.Database");
		
		SQLHandler sqlh = new SQLHandler(host, port, user, password, database);
		
		this.getServer().getLogger().info("BigBrother 1.0 has been enabled!");
	}
	
	public void onDisable() {
		this.getServer().getLogger().info("BigBrother 1.0 has been disabled!");
	}
	
	public void loadConfiguration() {
		this.getConfig().options().header("###############################################\nBig Brother\nFor Bukkit Build 1.13.2 R0.1\nBy: Sean Valley\n###############################################\n");
	    
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return true;
	}
}