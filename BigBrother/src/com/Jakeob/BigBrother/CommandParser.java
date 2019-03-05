package com.Jakeob.BigBrother;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Location;

public class CommandParser {
	
	//returns null if invalid args are entered
	public static String getConditions(String[] args, Location loc) {
		ArrayList<String> conditionArray = new ArrayList<String>();
		String conditions = "";
		
		for(String arg : args) {
			if(arg.length() > 2) {
				if(arg.substring(0, 2).equalsIgnoreCase("p:")){
					String input = arg.substring(2, arg.length());
					conditionArray.add("Player = '" + input + "'");
				}else if(arg.substring(0, 2).equalsIgnoreCase("r:")){
					String input = arg.substring(2, arg.length());
					int radius = 0;
					try{
						radius = Integer.parseInt(input);
					}catch(Exception e) {
						return null;
					}
					
					int x = loc.getBlockX();
					int y = loc.getBlockY();
					int z = loc.getBlockZ();
					
					int minX = x - radius;
					int maxX = x + radius;
					int minY = y - radius;
					int maxY = y + radius;
					int minZ = z - radius;
					int maxZ = z + radius;
					
					String xCon = "X >= " + minX + " AND X <= " + maxX;
					String yCon = "Y >= " + minY + " AND Y <= " + maxY;
					String zCon = "Z >= " + minZ + " AND Z <= " + maxZ;
					
					conditionArray.add(xCon + " AND " + yCon + " AND " + zCon);
				}else if(arg.substring(0, 2).equalsIgnoreCase("t:")){
					String input = arg.substring(2, arg.length());
					int days = 0;
					int hours = 0;
					int mins = 0;
					int secs = 0;
					
					if(input.contains("d")) {
						String daysStr = "";
						int dIndex = 0;
						for(char c : input.toCharArray()) {
							if(c != 'd') {
								daysStr += c;
							}else {
								break;
							}
							dIndex++;
						}
						try {
							days = Integer.parseInt(daysStr);
						}catch(Exception e) {
							return null;
						}
						
						input = input.substring(dIndex + 1);
					}
					
					if(input.contains("h")) {
						String hoursStr = "";
						int hIndex = 0;
						for(char c : input.toCharArray()) {
							if(c != 'h') {
								hoursStr += c;
							}else {
								break;
							}
							hIndex++;
						}
						try {
							hours = Integer.parseInt(hoursStr);
						}catch(Exception e) {
							return null;
						}
						
						input = input.substring(hIndex + 1);
					}
					
					if(input.contains("m")) {
						String minsStr = "";
						int mIndex = 0;
						for(char c : input.toCharArray()) {
							if(c != 'm') {
								minsStr += c;
							}else {
								break;
							}
							mIndex++;
						}
						try {
							mins = Integer.parseInt(minsStr);
						}catch(Exception e) {
							return null;
						}
						
						input = input.substring(mIndex + 1);
					}
					
					if(input.contains("s")) {
						String secsStr = "";
						int sIndex = 0;
						for(char c : input.toCharArray()) {
							if(c != 's') {
								secsStr += c;
							}else {
								break;
							}
							sIndex++;
						}
						try {
							secs = Integer.parseInt(secsStr);
						}catch(Exception e) {
							return null;
						}
						
						input = input.substring(sIndex + 1);
					}

					Date date = new Date();
					String dString = new SimpleDateFormat("dd").format(date);
					String hString = new SimpleDateFormat("HH").format(date);
					String mString = new SimpleDateFormat("mm").format(date);
					String sString = new SimpleDateFormat("ss").format(date);
					
					int sqlDay = Integer.parseInt(dString);
					int sqlHour = Integer.parseInt(hString);
					int sqlMin = Integer.parseInt(mString);
					int sqlSec = Integer.parseInt(sString);
					
					sqlDay -= days;
					sqlHour -= hours;
					sqlMin -= mins;
					sqlSec -= secs;
					
					if(sqlSec < 0) {
						sqlMin -= 1;
						sqlSec += 60;
					}
					
					if(sqlMin < 0) {
						sqlHour -= 1;
						sqlMin += 60;
					}
					
					if(sqlHour < 0) {
						sqlDay -= 1;
						sqlHour += 24;
					}
					
					String checkDate = new SimpleDateFormat("yyyy-MM-").format(date) + sqlDay + " " + sqlHour + ":" + sqlMin + ":" + sqlSec;
					conditionArray.add("Time >= '" + checkDate + "'");
				}else {
					return null;
				}
			}else {
				return null;
			}
		}
		
		if(conditionArray.size() == 0) {
			return null;
		}
		
		for(String condition : conditionArray) {
			conditions += condition + " AND ";
		}
		
		conditions = conditions.substring(0, conditions.length() - 5);
		
		return conditions;
	}
}