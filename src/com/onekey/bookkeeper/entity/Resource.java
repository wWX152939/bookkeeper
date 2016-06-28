package com.onekey.bookkeeper.entity;

import java.util.Date;

public class Resource {
	
	private int id;
	private int dir_id;
	private String name;
	private int number;
	private int parent_id;
    private int has_child;
    private int level;
    private Date time;
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Resource [id=" + id + ", dir_id=" + dir_id + ", name=" + name
				+ ", number=" + number + ", parent_id=" + parent_id
				+ ", has_child=" + has_child + ", level=" + level + ", time="
				+ time + "]";
	}
	public int getDir_id() {
		return dir_id;
	}
	public void setDir_id(int dir_id) {
		this.dir_id = dir_id;
	}
	public int getHas_child() {
		return has_child;
	}
	public void setHas_child(int has_child) {
		this.has_child = has_child;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
