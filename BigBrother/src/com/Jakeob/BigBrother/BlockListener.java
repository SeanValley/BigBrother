package com.Jakeob.BigBrother;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		String playerName = event.getPlayer().getDisplayName();
		Material type = event.getBlock().getType();
		Location loc = event.getBlock().getLocation();
		
		sqlh.addEntry(SQLHandler.getInsertStatement(playerName, "placed", type, loc));
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		String playerName = event.getPlayer().getDisplayName();
		Material type = event.getBlock().getType();
		Location loc = event.getBlock().getLocation();
		
		sqlh.addEntry(SQLHandler.getInsertStatement(playerName, "removed", type, loc));
	}
	
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