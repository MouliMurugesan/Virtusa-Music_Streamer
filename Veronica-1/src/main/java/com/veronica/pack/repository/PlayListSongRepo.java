package com.veronica.pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.veronica.pack.model.PlayListSongs;

public interface PlayListSongRepo extends JpaRepository<PlayListSongs,Long>{
	@Query("select pls from PlayListSongs pls where pls.playListid=?1")
	List<PlayListSongs> findByPlayListId(Long id);
	
	@Query("select pl from PlayListSongs pl where pl.playListid=?1 and pl.songid=?2")
	PlayListSongs findByPlaylistidAndSongId(Long playListid,Long songid);
	

}
