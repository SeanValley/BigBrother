package com.Jakeob.BigBrother;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SQLSender implements Runnable{
	private ArrayList<String> queries;
	private Connection connection;
	
	public SQLSender(Connection connection) {
		this.queries = new ArrayList<String>();
		this.connection = connection;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				TimeUnit.MILLISECONDS.sleep(5);
				if(!connection.isClosed() && queries.size() > 0) {
					Statement statement = this.connection.createStatement();
					if(queries.get(0).contains("DELETE")) {
						statement.executeUpdate(queries.get(0));
					}else {
						statement.execute(queries.get(0));
					}
					queries.remove(0);
				}
			} catch (Exception e) {
				BigBrother.logger.warning("Problem with connection in SQLSender!");
			}
		}
	}
	
	public void addQuery(String query) {
		this.queries.add(query);
	}
	
	public ArrayList<String> getQueries(){
		return this.queries;
	}
}