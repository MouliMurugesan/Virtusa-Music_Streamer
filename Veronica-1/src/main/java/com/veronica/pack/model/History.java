package com.veronica.pack.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class History {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private Long userid;
	private Long songid;
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
	public Long getSongid() {
		return songid;
	}
	public void setSongid(Long songid) {
		this.songid = songid;
	}
	public History(Long userid, Long songid) {
		super();
		this.userid = userid;
		this.songid = songid;
	}
	public History() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	
}
