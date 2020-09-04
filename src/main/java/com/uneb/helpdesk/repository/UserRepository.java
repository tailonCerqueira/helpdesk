package com.uneb.helpdesk.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uneb.helpdesk.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{	
	//@Query(value = "select * from users us inner join users_roles ur on us.id = ur.user_id where ur.role_id = :role_id and us.id not id(:user_id)", nativeQuery = true)
	@Query(value = "SELECT * FROM users us "
			+ "INNER JOIN users_roles ur on (us.id = ur.user_id) "
			+ "where ur.role_id = :role_id "
			+ "and us.id != :user_id ", nativeQuery = true)
	List<User> findAllWhereRoleEquals(@Param("role_id") Long role_id, @Param("user_id") Long user_id);
	
	
	User findByEmail(String email);
}
