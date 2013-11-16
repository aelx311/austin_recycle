package com.cs371m.austinrecycle;

public class checkboxModel {
	    
	    private String name;
	    private boolean selected;
	    
	    public checkboxModel(String name) {
	        this.name = name;
	    }
	    
	    public String getName() {
	        return name;
	    }
	    
	    public boolean isSelected() {
	        return selected;
	    }
	    
	    public void setSelected(boolean _selected) {
	        selected = _selected;
	    }

}
