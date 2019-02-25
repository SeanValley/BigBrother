package com.Jakeob.BigBrother;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandHandler {
	private BigBrother plugin;
	private SQLHandler sqlh;
	
	public CommandHandler(BigBrother plugin, SQLHandler sqlh) {
		this.plugin = plugin;
		this.sqlh = sqlh;
	}
	
	public void help(Player player) {
		if(player == null) {
			BigBrother.logger.info("-=Big Brother Help=-");
			BigBrother.logger.info("/bb help - Shows the help menu");
			BigBrother.logger.info("/bb log - Gives and removes log tool to/from player");
			BigBrother.logger.info("/bb undo - Undoes most recent rollback");
			BigBrother.logger.info("/bb history - Shows block modification history");
			BigBrother.logger.info("/bb rollback - Rolls back specified changes");
		}else {
			player.sendMessage(ChatColor.GOLD + "-=" + ChatColor.AQUA + "Big Brother Help" + ChatColor.GOLD + "=-");
			player.sendMessage(ChatColor.AQUA + "/bb help - " + ChatColor.WHITE + "Shows the help menu");
			player.sendMessage(ChatColor.AQUA + "/bb log - " + ChatColor.WHITE + "Gives player a log tool");
			player.sendMessage(ChatColor.AQUA + "/bb undo - " + ChatColor.WHITE + "Undoes most recent rollback");
			player.sendMessage(ChatColor.AQUA + "/bb history - " + ChatColor.WHITE + "Shows block modification history");
			player.sendMessage(ChatColor.AQUA + "/bb rollback - " + ChatColor.WHITE + "Rolls back specified changes");
		}
	}
	
	public void log(Player player) {
		if(player == null) {
			BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
		}else {
			Inventory inv = player.getInventory();
			if(!CommandHelper.hasLogTool(inv)) {
				if(CommandHelper.hasEmptySlot(inv)) {
					CommandHelper.giveLogTool(player);
					player.sendMessage(ChatColor.GREEN + "You have received the log tool!");
				}else {
					player.sendMessage(ChatColor.RED + "You don't have enough room in your inventory!");
				}
			}else {
				CommandHelper.removeLogTool(player);
				player.sendMessage(ChatColor.GREEN + "The log tool has been removed from your inventory!");
			}
		}
	}
	
	//Format: /bb history <p:[player]> <t:[_d_h_m_s]> <r:[radius]>
	public void history(Player player, String[] args) {
		if(player == null) {
			BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
		}else {
			String conditions = CommandParser.getConditions(args, player.getLocation());
			if(conditions != null) {
				ResultSet resultSet = sqlh.getResultSet(conditions);
				boolean hadResults = false;
				
				player.sendMessage("-- (" + ChatColor.YELLOW + "Results:" + ChatColor.WHITE + ") --");
				try {
					while(resultSet.next()) {
						hadResults = true;
						Timestamp timestamp = resultSet.getTimestamp("Time");
						Date date = new Date(timestamp.getTime());
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = sdf.format(date);
						
						String playerName = resultSet.getString("Player");
						String eventType = resultSet.getString("Event");
						if(eventType.equals("placed")) {
							eventType = ChatColor.GREEN + eventType;
						}else {
							eventType = ChatColor.RED + eventType;
						}
						String blockType = resultSet.getString("BlockType");
						
						int x = resultSet.getInt("X");
						int y = resultSet.getInt("Y");
						int z = resultSet.getInt("Z");
						
						String result = "[" + ChatColor.YELLOW + time + ChatColor.WHITE + "] " + playerName + " " + eventType + " " + ChatColor.WHITE + blockType
								+ "(" + x + "," + y + "," + z + ")";
						player.sendMessage(result);
					}
				} catch (SQLException e) {
					BigBrother.logger.warning("Problem grabbing results with history command!");
				}
				
				if(!hadResults) {
					player.sendMessage(ChatColor.RED + "No Results Found");
				}
			}else {
				player.sendMessage(ChatColor.RED + "Incorrect syntax for history arguments!");
			}			
		}
	}
	
	//Fomat: /bb rollback <p:[player]> <t:[_d_h_m_s]> <r:[radius]>
	public void rollback(Player player, String[] args) {
		if(player == null) {
			BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
		}else {
			String conditions = CommandParser.getConditions(args, player.getLocation());
			if(conditions != null) {
				ResultSet resultSet = sqlh.getResultSet(conditions + " ORDER BY Time DESC");
				boolean hadResults = false;
				
				try {
					int amount = 0;
					while(resultSet.next()) {
						hadResults = true;
						Timestamp timestamp = resultSet.getTimestamp("Time");
						Date date = new Date(timestamp.getTime());
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = sdf.format(date);
						
						String blockType = resultSet.getString("BlockType");
						Material type = Material.valueOf(blockType);
						
						String world = resultSet.getString("World");
						int x = resultSet.getInt("X");
						int y = resultSet.getInt("Y");
						int z = resultSet.getInt("Z");
						
						Location loc = new Location(plugin.getServer().getWorld(world), x, y, z);
						
						String event = resultSet.getString("Event");
						if(event.equals("placed")) {
							CommandHelper.removeBlock(loc);
						}else {
							CommandHelper.placeBlock(loc, type);
						}
						
						String delete = "DELETE FROM `BBLog` WHERE Time = '" + time + "' AND BlockType = '" + type
								+ "' AND World = '" + world + "' AND X = " + x + " AND Y = " + y + " AND Z = " + z + ";";
						sqlh.addEntry(delete);
						amount++;
					}
					
					if(!hadResults) {
						player.sendMessage(ChatColor.RED + "Nothing found under those parameters");
					}else {
						player.sendMessage(ChatColor.GREEN + "Rolled back " + amount + " actions!");
					}
				} catch (SQLException e) {
					BigBrother.logger.warning("Problem grabbing results with rollback command!");
				}
			}else {
				player.sendMessage(ChatColor.RED + "Incorrect syntax for rollback arguments!");
			}			
		}
	}
	
	public void undo(Player player) {
		if(player == null) {
			BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
		}else {
			//TODO: Undo recent rollback
		}
	}
}