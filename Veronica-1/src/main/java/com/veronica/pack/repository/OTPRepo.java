package com.veronica.pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veronica.pack.model.OTP;

@Repository
public interface OTPRepo extends JpaRepository<OTP,Long>{

	OTP findByEmailid(String emailid);
	

}
