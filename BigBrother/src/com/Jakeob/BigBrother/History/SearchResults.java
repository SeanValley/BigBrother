package com.Jakeob.BigBrother.History;

import java.util.ArrayList;

public class SearchResults {
	private ArrayList<String> results;
	
	public SearchResults() {
		this.results = new ArrayList<String>();
	}
	
	public void addResult(String result) {
		results.add(result);
	}
	
	public int getTotalPages() {
		int size = this.results.size();
		if(size % 5 == 0) {
			return size / 5;
		}else {
			return (size / 5) + 1;
		}
	}
	
	public String[] getPage(int pageNum) {
		pageNum -= 1;
		if(this.getTotalPages() >= (pageNum + 1) && pageNum >= 0) {
			String[] page = new String[5];
			
			for(int i=0;i<5;i++) {
				if(results.size() > i + (pageNum * 5)) {
					page[i] = results.get(i + (pageNum * 5));
				}
			}
			
			return page;
		}
		
		return null;
	}
}