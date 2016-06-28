package com.onekey.bookkeeper.entity;

import java.util.Date;

import org.kymjs.kjframe.database.annotate.Id;

public class DateDir {
	@Id()
	private int id;
	private Date name;
	
	public int getId() {
		return id;
	}
	@Override
	public String toString() {
		return "DateDir [id=" + id + ", name=" + name + "]";
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getName() {
		return name;
	}
	public void setName(Date name) {
		this.name = name;
	}
}
