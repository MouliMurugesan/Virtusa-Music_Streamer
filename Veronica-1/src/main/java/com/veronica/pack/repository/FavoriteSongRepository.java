package com.veronica.pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.veronica.pack.model.FavoriteSong;

@Repository
public interface FavoriteSongRepository extends JpaRepository<FavoriteSong,Long> {
	@Query("select f from FavoriteSong f where f.userid=?1 and f.songid=?2")
	FavoriteSong findByuseridAndfindBysongid(Long userid, Long songid);
	@Query("select fa from FavoriteSong fa where fa.userid=?1")
	List<FavoriteSong> findByUserId(Long userid);
	
}
