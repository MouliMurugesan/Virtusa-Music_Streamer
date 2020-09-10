package com.veronica.pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.veronica.pack.model.History;
@Repository
public interface HistoryRepository  extends JpaRepository<History,Long>{
	@Query("select h from History h where h.userid=?1 and h.songid=?2")
	History findByuseridAndfindBysongid(Long userid, Long songid);
	@Query("select hi from History hi where hi.userid=?1")
	List<History> findByUserId(Long userid);

}
