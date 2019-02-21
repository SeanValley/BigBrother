package com.Jakeob.BigBrother;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Big Brother by Sean Valley
 * Email: valleydsean@gmail.com
 * Github: github.com/SeanValley
 * 
 * TODO:
 * Read config
 * Establish connection to database
 * 
 * Initialize CommandParser
 * 
 * Create BlockListener and allow it to log changes
 * 
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
	
	public void onEnable() {
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