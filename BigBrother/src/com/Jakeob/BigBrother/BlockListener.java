package com.Jakeob.BigBrother;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockListener implements Listener{
	private SQLHandler sqlh;
	
	public BlockListener(SQLHandler sqlh) {
		this.sqlh = sqlh;
	}
	
	//Handles logging placed blocks and when the log tool is placed
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "BBLog")) {
			Player player = event.getPlayer();
			boolean hasPermission = true;
			
			hasPermission = player.isOp();
			if(!hasPermission) {
				hasPermission = player.hasPermission("bb.use");
			}
			
			if(hasPermission) {
				event.setCancelled(true);
				
				String world = event.getBlock().getWorld().getName();
				int x = event.getBlock().getX();
				int y = event.getBlock().getY();
				int z = event.getBlock().getZ();
				
				ResultSet resultSet = sqlh.getResultSet(world, x, y, z);
				boolean hadResults = false;
				
				player.sendMessage("-- (" + ChatColor.YELLOW + x + "," + y + "," + z + ChatColor.WHITE + ") --");
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
						
						String result = "[" + ChatColor.YELLOW + time + ChatColor.WHITE + "] " + playerName + " " + eventType + " " + ChatColor.WHITE + blockType;
						player.sendMessage(result);
					}
				} catch (SQLException e) {
					BigBrother.logger.warning("Problem grabbing results with log tool!");
				}
				
				if(!hadResults) {
					player.sendMessage(ChatColor.RED + "No Results Found");
				}
			}else {
				player.sendMessage(ChatColor.RED + "You don't have permission to use this tool!");
			}
		}else {
			String playerName = event.getPlayer().getDisplayName();
			Material type = event.getBlock().getType();
			Location loc = event.getBlock().getLocation();
			
			sqlh.addEntry(SQLHandler.getInsertStatement(playerName, "placed", type, loc));
		}
	}
	
	//Handles logging broken blocks
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		String playerName = event.getPlayer().getDisplayName();
		Material type = event.getBlock().getType();
		Location loc = event.getBlock().getLocation();
		
		sqlh.addEntry(SQLHandler.getInsertStatement(playerName, "removed", type, loc));
	}
	
	//Handles logging blocks that have been exploded
	@EventHandler
	public void onBlockExplode(EntityExplodeEvent event) {
		List<Block> blockList = event.blockList();
		
		for(Block block : blockList) {
			Material type = block.getType();
			Location loc = block.getLocation();
			
			sqlh.addEntry(SQLHandler.getInsertStatement("EXPLOSION", "removed", type, loc));
		}
	}
}