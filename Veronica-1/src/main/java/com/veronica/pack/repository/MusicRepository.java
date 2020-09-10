package com.veronica.pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.veronica.pack.model.Music;

@Repository
public interface MusicRepository extends JpaRepository<Music,Long> {
	@Query("select m from Music m where m.id=?1")
	Music findByID(Long songid);

	List<Music> findByLanguage(String language);

	Music findByName(String name);

	List<Music> findByGenres(String genres);
}
