package me.artograd.javamongoapi.model;

public class NestedLocation {
	private String name;
	private NestedLocation child;
	
	public NestedLocation(String name, NestedLocation child) {
		super();
		this.name = name;
		this.child = child;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public NestedLocation getChild() {
		return child;
	}
	public void setChild(NestedLocation child) {
		this.child = child;
	}
}
