package com.Jakeob.BigBrother;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandHelper {
	public static boolean hasEmptySlot(Inventory inv) {
		for(ItemStack is : inv) {
			if(is == null) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean hasLogTool(Inventory inv) {
		for(ItemStack is : inv) {
			if(is != null) {
				String itemName = is.getItemMeta().getDisplayName();
				if(itemName.equals(ChatColor.BLUE + "BBLog")) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void giveLogTool(Player player) {
		Inventory inv = player.getInventory();
		
		ItemStack logTool = new ItemStack(Material.OAK_LOG, 1);
		ItemMeta toolMeta = logTool.getItemMeta();
		toolMeta.setDisplayName(ChatColor.BLUE + "BBLog");
		
		List<String> lore = new ArrayList<String>();
		lore.add("Place for info!");
		toolMeta.setLore(lore);
		
		logTool.setItemMeta(toolMeta);
		
		inv.addItem(logTool);
	}
	
	public static void removeLogTool(Player player) {
		Inventory inv = player.getInventory();
		
		for(ItemStack is : inv) {
			if(is != null) {
				String itemName = is.getItemMeta().getDisplayName();
				if(itemName.equals(ChatColor.BLUE + "BBLog")) {
					inv.remove(is);
					break;
				}
			}
		}
	}
	
	public static void removeBlock(Location loc) {
		loc.getBlock().setType(Material.AIR);
	}
	
	public static void placeBlock(Location loc, Material type) {
		loc.getBlock().setType(type);
	}
}
