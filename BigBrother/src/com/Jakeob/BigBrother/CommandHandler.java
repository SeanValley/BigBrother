package com.Jakeob.BigBrother;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandHandler {
	
	public static void help(Player player) {
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
	
	public static void log(Player player) {
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
	
	public static void undo(Player player) {
		if(player == null) {
			BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
		}else {
			//TODO: Undo recent rollback
		}
	}
	
	public static void history(Player player, String[] args) {
		if(player == null) {
			BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
		}else {
			//TODO: Give player history based on specified parameters
		}
	}
	
	public static void rollback(Player player, String[] args) {
		if(player == null) {
			BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
		}else {
			//TODO: roll back based on specified parameters
		}
	}
}