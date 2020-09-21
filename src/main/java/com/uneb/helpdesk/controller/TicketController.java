package com.uneb.helpdesk.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uneb.helpdesk.model.Interaction;
import com.uneb.helpdesk.model.Role;
import com.uneb.helpdesk.model.Ticket;
import com.uneb.helpdesk.model.User;
import com.uneb.helpdesk.service.RoleService;
import com.uneb.helpdesk.service.TicketService;
import com.uneb.helpdesk.service.UserService;

@Controller
@RequestMapping("/tickets")
public class TicketController {
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	public TicketController(TicketService ticketService, RoleService roleService, UserService userService) {
		this.ticketService = ticketService;
		this.roleService = roleService;
		this.userService = userService;
	}
	
	@GetMapping
	public String index(Model model) {
		model.addAttribute("list", this.ticketService.findAll());
		//User userLoggedIn = this.userService.findCurrentUser();
		model.addAttribute("userLoggedIn", this.userService.findCurrentUser());
		//System.out.print(userLoggedIn.getId());
		return "tickets/index";
	}
	
	@GetMapping("{id}")
	public String show(@PathVariable("id") Long id, Model model) {
		Ticket ticket = this.ticketService.show(id);
		List<Interaction> interactions = ticket.getInteractions();
		
		model.addAttribute("interactions", interactions);
		model.addAttribute("ticket", this.ticketService.show(id));
		model.addAttribute("interaction", new Interaction());
		model.addAttribute("userLoggedIn", this.userService.findCurrentUser());
		return "tickets/show";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		Ticket ticket = this.ticketService.show(id);
		List<Interaction> interactions = ticket.getInteractions();
		
		//model = this.ticketService.findAllTechinician(model);
		//model = this.ticketService.findAllTechinician(model);
		Role adminRole = this.roleService.findByName("ADMIN");		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
				
		User userLogged = this.userService.findByEmail(userName);		
		model.addAttribute("userLoggedIn", userLogged);
		model.addAttribute("techs", this.userService.findAllWhereRoleEquals(adminRole.getId(), userLogged.getId()));
				
		model.addAttribute("ticket", this.ticketService.show(id));
		model.addAttribute("interactions_count", interactions.size());
		
		return "tickets/edit";
	}
	
	@GetMapping("/new")
	public String create(Model model) {
		//model = this.ticketService.findAllTechinician(model);
		Role adminRole = this.roleService.findByName("ADMIN");
		System.out.println(adminRole.getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		
		User userLogged = this.userService.findByEmail(userName);
		System.out.println(userLogged.getId());
		model.addAttribute("techs", this.userService.findAllWhereRoleEquals(adminRole.getId(), userLogged.getId()));
		
		model.addAttribute("ticket", new Ticket());
		return "tickets/create";									
	}
	
	@PostMapping
	public String save(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "tickets/create";
		}
		
		this.ticketService.create(ticket);
		return "redirect:/tickets";
	}
	
	//@PutMapping
	@PostMapping("/update/{id}")
	public String update (@PathVariable("id") Long id, @ModelAttribute("ticket") Ticket ticket, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "tickets/edit";
		}
		
		this.ticketService.update(id, ticket);
		
		return "redirect:/tickets";
		
	}
	
	@DeleteMapping
	public String delete(@PathVariable("id") Long id, Model model) {
		this.ticketService.delete(id);
		return "redirect:/tickets";
	}
}
