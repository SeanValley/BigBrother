package com.Jakeob.BigBrother;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Big Brother by Sean Valley
 * Email: valleydsean@gmail.com
 * Github: github.com/SeanValley
 * 
 * TODO:
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
		PluginManager pm = this.getServer().getPluginManager();
		
		loadConfiguration();

		String host = this.getConfig().getString("MySQL.Host");
		String port = this.getConfig().getString("MySQL.Port");
		String user = this.getConfig().getString("MySQL.User");
		String password = this.getConfig().getString("MySQL.Password");
		String database = this.getConfig().getString("MySQL.Database");
		
		SQLHandler sqlh = new SQLHandler(host, port, user, password, database);
		
		pm.registerEvents(new BlockListener(sqlh), this);
		
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
		if(cmd.getName().equalsIgnoreCase("bb")) {
			Player player = null;
			if(sender instanceof Player) {
				player = (Player) sender;
			}
			
			if(args.length == 0) {
				CommandHandler.help(player);
			}else if(args[0].equalsIgnoreCase("log")) {
				CommandHandler.log(player);
			}else if(args[0].equalsIgnoreCase("undo")) {
				CommandHandler.undo(player);
			}else if(args[0].equalsIgnoreCase("history")) {
				String[] newArgs = new String[args.length - 1];
				for(int i=1;i<args.length;i++) {
					newArgs[i - 1] = args[i];
				}
				CommandHandler.history(player, newArgs);
			}else if(args[0].equalsIgnoreCase("rollback")) {
				String[] newArgs = new String[args.length - 1];
				for(int i=1;i<args.length;i++) {
					newArgs[i - 1] = args[i];
				}
				CommandHandler.history(player, newArgs);
			}else {
				CommandHandler.help(player);
			}
			return true;
		}
		return false;
	}
}