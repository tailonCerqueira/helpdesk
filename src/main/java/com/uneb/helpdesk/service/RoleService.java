package com.uneb.helpdesk.service;

import java.util.List;

import com.uneb.helpdesk.model.Role;

public interface RoleService {
	
	public List<Role> findAll();
	public Role create(Role role);
	public Boolean delete(Long id);
	public Role findByName(String name);
}
