package com.Jakeob.BigBrother;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Jakeob.BigBrother.History.SearchResults;
import com.Jakeob.BigBrother.Rollbacks.Rollback;

public class PlayerListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		String name = event.getPlayer().getName();
		CommandHandler.rollbacks.put(name, new ArrayList<Rollback>());
		CommandHandler.searchResults.put(name, new SearchResults());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		String name = event.getPlayer().getName();
		CommandHandler.rollbacks.remove(name);
		CommandHandler.searchResults.remove(name);
	}
	
	@EventHandler
	public void onPlayerKicked(PlayerKickEvent event) {
		String name = event.getPlayer().getName();
		CommandHandler.rollbacks.remove(name);
		CommandHandler.searchResults.remove(name);
	}
}