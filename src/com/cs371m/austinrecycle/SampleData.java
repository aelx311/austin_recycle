package com.cs371m.austinrecycle;

public class SampleData {
	 
	private String name;
 
	private boolean selected;
 
	public SampleData(String name, boolean selected) {
		super();
		this.name = name;
		this.selected = selected;
	}
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public boolean isSelected() {
		return selected;
	}
 
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
 
}