package com.Jakeob.BigBrother.Rollbacks;

public class Action {
	private String playerName;
	private String time;
	private boolean removed;
	private String blockType;
	private String world;
	private int x;
	private int y;
	private int z;
	
	public Action(String playerName, String time, boolean removed, String blockType, String world, int x, int y, int z) {
		this.playerName = playerName;
		this.time = time;
		this.removed = removed;
		this.blockType = blockType;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	public String getTime() {
		return this.time;
	}
	
	public boolean wasRemoved() {
		return this.removed;
	}
	
	public String getBlockType() {
		return this.blockType;
	}
	
	public String getWorld() {
		return this.world;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
}