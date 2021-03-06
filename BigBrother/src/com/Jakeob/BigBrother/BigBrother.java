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
 */

public class BigBrother extends JavaPlugin{
	public static Logger logger = null;
	
	private CommandHandler cHandler;
	
	public void onEnable() {
		logger = this.getServer().getLogger();
		PluginManager pm = this.getServer().getPluginManager();
		
		loadConfiguration();

		String host = this.getConfig().getString("MySQL.Host");
		String port = this.getConfig().getString("MySQL.Port");
		String user = this.getConfig().getString("MySQL.User");
		String password = this.getConfig().getString("MySQL.Password");
		String database = this.getConfig().getString("MySQL.Database");
		
		if(host.equals("0.0.0.0")) {
			BigBrother.logger.warning("Please configure your database information");
			BigBrother.logger.warning("Shutting down BigBrother...");
			this.setEnabled(false);
		}else {
			SQLHandler sqlh = new SQLHandler(host, port, user, password, database, this);
			
			if(sqlh.couldInitConnect()) {
				pm.registerEvents(new BlockListener(sqlh), this);
				pm.registerEvents(new PlayerListener(), this);
				
				this.cHandler = new CommandHandler(this, sqlh);
				
				this.getServer().getLogger().info("BigBrother 1.0 has been enabled!");
			}
		}
	}
	
	public void onDisable() {
		this.getServer().getLogger().info("BigBrother 1.0 has been disabled!");
	}
	
	public void loadConfiguration() {
		this.getConfig().options().header(
				  "###############################################\n"
				+ "Big Brother 1.0\n"
				+ "For Bukkit Build 1.13.2\n"
				+ "By: Sean Valley (jakeob22)\n"
				+ "###############################################\n");
	    
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
	public void manuallyDisable() {
		this.setEnabled(false);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("bb")) {
			Player player = null;
			boolean hasPermission = true;
			if(sender instanceof Player) {
				player = (Player) sender;
				hasPermission = player.isOp();
				if(!hasPermission) {
					hasPermission = player.hasPermission("bb.use");
				}
			}
			
			if(args.length == 0) {
				this.cHandler.help(player, hasPermission);
			}else if(args[0].equalsIgnoreCase("log")) {
				this.cHandler.log(player, hasPermission);
			}else if(args[0].equalsIgnoreCase("undo")) {
				this.cHandler.undo(player, hasPermission);
			}else if(args[0].equalsIgnoreCase("history")) {
				String[] newArgs = new String[args.length - 1];
				for(int i=1;i<args.length;i++) {
					newArgs[i - 1] = args[i];
				}
				this.cHandler.history(player, newArgs, hasPermission);
			}else if(args[0].equalsIgnoreCase("rollback")) {
				String[] newArgs = new String[args.length - 1];
				for(int i=1;i<args.length;i++) {
					newArgs[i - 1] = args[i];
				}
				this.cHandler.rollback(player, newArgs, hasPermission);
			}else if(args[0].equalsIgnoreCase("page")) {
				String[] newArgs = new String[args.length - 1];
				for(int i=1;i<args.length;i++) {
					newArgs[i - 1] = args[i];
				}
				this.cHandler.page(player, newArgs, hasPermission);
			}else {
				this.cHandler.help(player, hasPermission);
			}
			return true;
		}
		return false;
	}
}