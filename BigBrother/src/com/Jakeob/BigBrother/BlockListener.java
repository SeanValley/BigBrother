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
	public void onBlockBreak(BlockBreakEvent event) {
		String playerName = event.getPlayer().getDisplayName();
		Material type = event.getBlock().getType();
		Location loc = event.getBlock().getLocation();
		
		sqlh.addEntry(playerName, "removed", type, loc);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		String playerName = event.getPlayer().getDisplayName();
		Material type = event.getBlock().getType();
		Location loc = event.getBlock().getLocation();
		
		sqlh.addEntry(playerName, "placed", type, loc);
	}
	
	//Blocks exploding still aren't registered to database
	@EventHandler
	public void onBlockExplode(EntityExplodeEvent event) {
		BigBrother.logger.info("Ran explode event");
		List<Block> blockList = event.blockList();
		
		for(Block block : blockList) {
			BigBrother.logger.info("looped");
			Material type = block.getType();
			Location loc = block.getLocation();
			
			sqlh.addEntry("EXPLOSION", "removed", type, loc);
		}
	}
}