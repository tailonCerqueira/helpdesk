package com.uneb.helpdesk.service;

import java.util.List;

import com.uneb.helpdesk.model.User;

public interface UserService {

	public List<User> findAll();
	public User create(User user);
	public Boolean delete(Long id);
	public Boolean update(Long id, User user);
	public User show(Long id);
	public List<User> findAllWhereRoleEquals(Long role_id, Long user_id);
	//
	public User findByEmail(String userName);
	public User findCurrentUser();
}
