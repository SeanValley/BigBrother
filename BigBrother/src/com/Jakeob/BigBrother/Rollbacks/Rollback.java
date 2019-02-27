package com.Jakeob.BigBrother.Rollbacks;

import java.util.ArrayList;

public class Rollback {
	private ArrayList<Action> actions;
	
	public Rollback(ArrayList<Action> actions) {
		this.actions = actions;
	}
	
	public Action getAction(int index) {
		try{
			return actions.get(index);
		}catch(Exception e) {
			return null;
		}
	}
	
	public ArrayList<Action> getAllActions(){
		return this.actions;
	}
}
