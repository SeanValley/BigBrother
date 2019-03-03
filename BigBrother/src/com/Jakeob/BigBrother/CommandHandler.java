package com.Jakeob.BigBrother;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.Jakeob.BigBrother.History.SearchResults;
import com.Jakeob.BigBrother.Rollbacks.Action;
import com.Jakeob.BigBrother.Rollbacks.Rollback;

public class CommandHandler {
	public static HashMap<String, ArrayList<Rollback>> rollbacks = new HashMap<String, ArrayList<Rollback>>();
	public static HashMap<String, SearchResults> searchResults = new HashMap<String, SearchResults>();
	
	private BigBrother plugin;
	private SQLHandler sqlh;
	
	public CommandHandler(BigBrother plugin, SQLHandler sqlh) {
		this.plugin = plugin;
		this.sqlh = sqlh;
	}
	
	public void help(Player player, boolean hasPermission) {
		if(player == null) {
			BigBrother.logger.info("-=Big Brother Help=-");
			BigBrother.logger.info("/bb help - Shows the help menu");
			BigBrother.logger.info("/bb log - Gives and removes log tool to/from player");
			BigBrother.logger.info("/bb undo - Undoes most recent rollback");
			BigBrother.logger.info("/bb history - Shows block modification history");
			BigBrother.logger.info("/bb rollback - Rolls back specified changes");
		}else {
			if(hasPermission) {
				player.sendMessage(ChatColor.GOLD + "-=" + ChatColor.AQUA + "Big Brother Help" + ChatColor.GOLD + "=-");
				player.sendMessage(ChatColor.AQUA + "/bb help - " + ChatColor.WHITE + "Shows the help menu");
				player.sendMessage(ChatColor.AQUA + "/bb log - " + ChatColor.WHITE + "Gives player a log tool");
				player.sendMessage(ChatColor.AQUA + "/bb undo - " + ChatColor.WHITE + "Undoes most recent rollback");
				player.sendMessage(ChatColor.AQUA + "/bb rollback - " + ChatColor.WHITE + "Rolls back specified changes");
				player.sendMessage(ChatColor.AQUA + "/bb history - " + ChatColor.WHITE + "Shows block modification history");
				player.sendMessage(ChatColor.AQUA + "/bb page [#] - " + ChatColor.WHITE + "Shows different pages of history results");
			}else {
				player.sendMessage(ChatColor.RED + "You don't have permission for this command!");
			}
		}
	}
	
	public void log(Player player, boolean hasPermission) {
		if(hasPermission) {
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
		}else {
			player.sendMessage(ChatColor.RED + "You don't have permission for this command!");
		}
	}
	
	//Fomat: /bb rollback <p:[player]> <t:[_d_h_m_s]> <r:[radius]>
	public void rollback(Player player, String[] args, boolean hasPermission) {
		if(hasPermission) {
			if(player == null) {
				BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
			}else {
				String conditions = CommandParser.getConditions(args, player.getLocation());
				if(conditions != null) {
					ResultSet resultSet = sqlh.getResultSet(conditions + " ORDER BY Time DESC");
					boolean hadResults = false;
					
					try {
						ArrayList<Action> actions = new ArrayList<Action>();
						int amount = 0;
						while(resultSet.next()) {
							hadResults = true;
							Timestamp timestamp = resultSet.getTimestamp("Time");
							Date date = new Date(timestamp.getTime());
							
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String time = sdf.format(date);
							
							String playerName = resultSet.getString("Player");
							
							String blockType = resultSet.getString("BlockType");
							Material type = Material.valueOf(blockType);
							
							String world = resultSet.getString("World");
							int x = resultSet.getInt("X");
							int y = resultSet.getInt("Y");
							int z = resultSet.getInt("Z");
							
							Location loc = new Location(plugin.getServer().getWorld(world), x, y, z);
							
							String event = resultSet.getString("Event");
							
							boolean wasRemoved = true;
							if(event.equals("placed")) {
								wasRemoved = false;
								CommandHelper.removeBlock(loc);
								Action action = new Action(playerName, time, wasRemoved, blockType, world, x, y, z);
								actions.add(action);
							}else {
								CommandHelper.placeBlock(loc, type);
								Action action = new Action(playerName, time, wasRemoved, blockType, world, x, y, z);
								actions.add(action);
							}
							
							String delete = "DELETE FROM `BBLog` WHERE Time = '" + time + "' AND BlockType = '" + type
									+ "' AND World = '" + world + "' AND X = " + x + " AND Y = " + y + " AND Z = " + z + ";";
							sqlh.addEntry(delete);
							amount++;
						}
						
						if(!hadResults) {
							player.sendMessage(ChatColor.RED + "Nothing found under those parameters");
						}else {
							Rollback rollback = new Rollback(actions);
							ArrayList<Rollback> playerRollbacks = rollbacks.get(player.getName());
							if(playerRollbacks != null) {
								rollbacks.get(player.getName()).add(rollback);
							}else {
								ArrayList<Rollback> newRollbacks = new ArrayList<Rollback>();
								newRollbacks.add(rollback);
								rollbacks.put(player.getName(), newRollbacks);
							}
							player.sendMessage(ChatColor.GREEN + "Rolled back " + amount + " actions!");
						}
					} catch (SQLException e) {
						BigBrother.logger.warning("Problem grabbing results with rollback command!");
					}
				}else {
					player.sendMessage(ChatColor.RED + "Incorrect syntax for rollback arguments!");
				}			
			}
		}else {
			player.sendMessage(ChatColor.RED + "You don't have permission for this command!");
		}
	}
	
	//Format: /bb history <p:[player]> <t:[_d_h_m_s]> <r:[radius]>
	public void history(Player player, String[] args, boolean hasPermission) {
		if(hasPermission) {
			if(player == null) {
				BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
			}else {
				String conditions = CommandParser.getConditions(args, player.getLocation());
				if(conditions != null) {
					ResultSet resultSet = sqlh.getResultSet(conditions);
					boolean hadResults = false;
					
					SearchResults sResults = new SearchResults();
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
							sResults.addResult(result);
						}
						CommandHandler.searchResults.put(player.getName(), sResults);
					} catch (SQLException e) {
						BigBrother.logger.warning("Problem grabbing results with history command!");
					}
					
					if(!hadResults) {
						player.sendMessage(ChatColor.RED + "No Results Found");
					}else {
						player.sendMessage("-- (" + ChatColor.YELLOW + "Results (Page 1 of " + sResults.getTotalPages() + ")" + ChatColor.WHITE + ") --");
						String[] page = sResults.getPage(1);
						for(int i=0;i<page.length;i++) {
							player.sendMessage(page[i]);
						}
					}
				}else {
					player.sendMessage(ChatColor.RED + "Incorrect syntax for history arguments!");
				}			
			}
		}else {
			player.sendMessage(ChatColor.RED + "You don't have permission for this command!");
		}
	}
	
	public void page(Player player, String[] args, boolean hasPermission) {
		if(hasPermission) {
			if(player == null) {
				BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
			}else {
				if(args.length == 1) {
					try {
						int pageNum = Integer.parseInt(args[0]);
						SearchResults sResults = CommandHandler.searchResults.get(player.getName());
						String[] page = sResults.getPage(pageNum);
						if(page != null) {
							player.sendMessage("-- (" + ChatColor.YELLOW + "Results (Page " + pageNum + " of " + sResults.getTotalPages() + ")" + ChatColor.WHITE + ") --");
							for(int i=0;i<page.length;i++) {
								if(page[i] != null) {
									player.sendMessage(page[i]);
								}
							}
						}else {
							player.sendMessage(ChatColor.RED + "Please enter a page between 1 and " + sResults.getTotalPages());
						}
						
					}catch(NumberFormatException e) {
						player.sendMessage(ChatColor.RED + "Invalid page number!");
					}
				}else {
					player.sendMessage(ChatColor.RED + "Incorrect syntax for page argument!");
				}
			}
		}
	}
	
	public void undo(Player player, boolean hasPermission) {
		if(hasPermission) {
			if(player == null) {
				BigBrother.logger.info(ChatColor.RED + "This command is only useable by players!");
			}else {
				String name = player.getName();
				ArrayList<Rollback> playerRBs = rollbacks.get(name);
				if(playerRBs != null) {
					BigBrother.logger.info("Had rollbacks to make");
					BigBrother.logger.info("rollback list size: " + rollbacks.get(name).size());
					int rbsSize = playerRBs.size();
					Rollback recentRB = playerRBs.get(rbsSize - 1);
					for(Action action : recentRB.getAllActions()) {
						String playerName = action.getPlayerName();
						String time = action.getTime();
						String blockType = action.getBlockType();
						String world = action.getWorld();
						int x = action.getX();
						int y = action.getY();
						int z = action.getZ();
						
						String event = "placed";
						if(action.wasRemoved()) {
							event = "removed";
							CommandHelper.removeBlock(new Location(plugin.getServer().getWorld(world), x, y, z));
						}else {
							CommandHelper.placeBlock(new Location(plugin.getServer().getWorld(world), x, y, z), Material.getMaterial(blockType));
						}
						
						String entry = SQLHandler.getInsertStatement(playerName, time, event, blockType, world, x, y, z);
						sqlh.addEntry(entry);
					}
					
					@SuppressWarnings("unchecked")
					ArrayList<Rollback> playerRollbacks = (ArrayList<Rollback>) rollbacks.get(name).clone();
					playerRollbacks.remove(playerRollbacks.size() - 1);
					rollbacks.put(name, playerRollbacks);
				}else {
					player.sendMessage(ChatColor.RED + "No rollbacks to undo");
				}
			}
		}else {
			player.sendMessage(ChatColor.RED + "You don't have permission for this command!");
		}
	}
}