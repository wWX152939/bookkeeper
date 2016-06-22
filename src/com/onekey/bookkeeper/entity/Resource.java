package com.onekey.bookkeeper.entity;

public class Resource {
	private int resource_id;
	private String name;
	private int number;
	private int parent_id;
    private boolean has_child;
    private int level;
    private boolean expanded;
	
	public boolean isHas_child() {
		return has_child;
	}
	public void setHas_child(boolean has_child) {
		this.has_child = has_child;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public int getResource_id() {
		return resource_id;
	}
	public void setResource_id(int resource_id) {
		this.resource_id = resource_id;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
}
