package com.uneb.helpdesk.service;

import com.uneb.helpdesk.model.Interaction;

public interface InteractionService {
	public Interaction create(Interaction interaction, Long ticketId);
	public Boolean delete(Long id, Long ticketId);
}
