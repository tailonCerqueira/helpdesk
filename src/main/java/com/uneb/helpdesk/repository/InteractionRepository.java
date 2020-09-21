package com.uneb.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uneb.helpdesk.model.Interaction;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long>{	
	
}
