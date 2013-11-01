package com.cs371m.austinrecycle;

public class MaterialItem {
	private int _icon;
	private String _name;
	
	public MaterialItem(int icon, String name) {
		_icon = icon;
		_name = name;
	}
	
	public int getIcon() {
		return _icon;
	}
	
	public String getName() {
		return _name;
	}
}
