package com.veronica.pack.favDto;

public class FavoriteSongDto {

	private Long id;
	private String songName;
	private String language;
	public Long getId() {
		return id;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	
	public FavoriteSongDto(Long id, String songName, String language) {
		super();
		this.id = id;
		this.songName = songName;
		this.language = language;
	}

	public FavoriteSongDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
