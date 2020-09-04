package com.uneb.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uneb.helpdesk.model.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction, Long>{

	////////////////////////////
	Interaction findOne(Long id);

	
	
}
