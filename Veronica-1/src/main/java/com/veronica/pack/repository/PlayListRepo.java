package com.veronica.pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.veronica.pack.model.PlayList;

public interface PlayListRepo extends JpaRepository<PlayList,Long> {
	@Query("select pl from PlayList pl where pl.userid=?1")
	List<PlayList> findByUserId(Long userid);
	@Query("select p from PlayList p where p.id=?1")
	PlayList findByid(Long id);

}
