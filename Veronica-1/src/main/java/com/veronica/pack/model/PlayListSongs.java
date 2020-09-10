package com.veronica.pack.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PlayListSongs {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private Long playListid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPlayListid() {
		return playListid;
	}
	public void setPlayListid(Long playListid) {
		this.playListid = playListid;
	}
	public Long getSongid() {
		return songid;
	}
	public void setSongid(Long songid) {
		this.songid = songid;
	}
	private Long songid;
	public PlayListSongs(Long playListid, Long songid) {
		super();
		this.playListid = playListid;
		this.songid = songid;
	}
	public PlayListSongs() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
