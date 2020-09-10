package com.veronica.pack.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PlayList {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private Long userid;
	private String playListName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getPlayListName() {
		return playListName;
	}
	public void setPlayListName(String playListName) {
		this.playListName = playListName;
	}
	public PlayList(Long userid, String playListName) {
		super();
		this.userid = userid;
		this.playListName = playListName;
	}
	public PlayList() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
