package com.pokerface.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pokerface.identity_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	boolean existsByUsername(String username);
}
