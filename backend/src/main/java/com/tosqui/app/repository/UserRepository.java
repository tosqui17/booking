package com.tosqui.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tosqui.app.io.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findUserByEmail(String email);

	UserEntity findUserByUsername(String user);

	UserEntity findUserByUsernameOrEmail(String username, String email);

	void deleteAllBySoftDeleted(boolean bool);
}
