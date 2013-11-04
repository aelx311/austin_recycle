package com.cs371m.austinrecycle;

public class MaterialItem {
	private int _icon;
	private String _name;
//	private boolean _checked;
	
//	public MaterialItem(int icon, String name, boolean checked) {
//		_icon = icon;
//		_name = name;
//		_checked = checked;
//	}
	
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
	
//	public boolean getChecked() {
//		return _checked;
//	}
}
