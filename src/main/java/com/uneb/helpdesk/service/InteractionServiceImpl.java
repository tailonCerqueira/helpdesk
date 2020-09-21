package com.uneb.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uneb.helpdesk.model.Interaction;
import com.uneb.helpdesk.model.Ticket;
import com.uneb.helpdesk.model.User;
import com.uneb.helpdesk.repository.InteractionRepository;
import com.uneb.helpdesk.repository.UserRepository;

@Service
public class InteractionServiceImpl implements InteractionService{

	@Autowired
	private InteractionRepository interactionRepository;

	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private UserRepository userRepository;
	public InteractionServiceImpl(InteractionRepository interactionRepository
			, UserRepository userRepository, TicketService ticketService) {
		this.interactionRepository = interactionRepository;
		this.userRepository = userRepository;
		this.ticketService = ticketService;
	}
	
	@Override
	public Interaction create(Interaction interaction, Long ticketId) {		
		
		Ticket ticket = this.ticketService.show(ticketId);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		User userLogged = this.userRepository.findByEmail(username);
		
		interaction.setTicket(ticket);
		interaction.setUserInteraction(userLogged);
		
		
		return this.interactionRepository.save(interaction);
	}

	@Override
	public Boolean delete(Long id, Long ticketId) { 
		Interaction interaction = this.findById(id);
		
		if(interaction != null) {
			this.interactionRepository.delete(interaction);			
		}
						
		return false;
	}
	
	private Interaction findById(Long id) {
		return this.interactionRepository.findById(id).orElse(null);
	}

}
