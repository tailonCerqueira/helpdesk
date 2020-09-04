package com.uneb.helpdesk.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.uneb.helpdesk.model.Role;
import com.uneb.helpdesk.model.Ticket;
import com.uneb.helpdesk.model.User;
import com.uneb.helpdesk.repository.TicketRepository;
import com.uneb.helpdesk.repository.UserRepository;

@Service
public class TicketServiceImpl implements TicketService{
	//private final Long ROLE_ID = (long) 4;
	private final String ROLE_NAME = "ADMIN";
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository, UserService userService, RoleService roleService) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.userService = userService;
		this.roleService = roleService;
	}
	@Override
	public List<Ticket> findAll() { 
		return (List<Ticket>) this.ticketRepository.findAll();
	}

	@Override
	public Ticket create(Ticket ticket) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		
		User userLogged = this.userRepository.findByEmail(userName);
		ticket.setUserOpen(userLogged);
		return this.ticketRepository.save(ticket);
	}

	@Override
	public Boolean delete(Long id) {
		Ticket ticket = findById(id);
		
		if(ticket != null) {
			this.ticketRepository.delete(ticket);
			return true;
		}
		return false;
	}

	@Override
	public Boolean update(Long id, Ticket ticket) {
		Ticket ticketExists = findById(id);
		if(ticketExists != null) {
			ticketExists.setId(ticket.getId());
			ticketExists.setName(ticket.getName());
			ticketExists.setDescription(ticket.getDescription());
			ticketExists.setFinished(ticket.getFinished());
			//ticketExists.setTechnician(ticket.getTechnician());
			
			if(ticket.getFinished()) {
				ticketExists.setClosed(new Date());
			}
			
			this.ticketRepository.save(ticket);
			
			return true;
		}
		return false;
	}

	@Override
	public Ticket show(Long id) {
		return this.findById(id);
	}
	
	//No momento está no controller
	@Override
	public Model findAllTechinician(Model model) {
		
		Role adminRole = this.roleService.findByName(ROLE_NAME);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		
		User userLogged = this.userRepository.findByEmail(userName);
		model.addAttribute("techs", this.userService.findAllWhereRoleEquals(adminRole.getId(), userLogged.getId()));
		//model.addAttribute("techs", this.userService.findAllWhereRoleEquals(role_id));
		
		return model;
	}

	private Ticket findById(Long id) {
		return this.ticketRepository.findById(id).orElse(null);
	}
}
