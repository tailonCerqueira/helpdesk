package com.uneb.helpdesk.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.uneb.helpdesk.model.User;
import com.uneb.helpdesk.service.UserService;
import com.uneb.helpdesk.model.Role;

@Controller
public class AuthController {
	@Autowired
	private UserService userService;
	
	public AuthController(UserService userService) {
		this.userService = userService;	
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "auth/login";
	}
	
	@GetMapping("/registration")
	public String registration(Model model) {
		
		model.addAttribute("user", new User());
		return "auth/registration";
		
	}
	
	@PostMapping("/cadastrar")
	public ModelAndView save(@ModelAttribute(value = "user") @Valid User user, 
			BindingResult indingResult, Model model) {
		
		if (indingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("auth/registrar");
			return mv;			
		}
		
		Role role = new Role();
		
		role.setName("USER");						
		this.userService.create(user);		
		
		ModelAndView mv = new ModelAndView("auth/login");
		return mv;			
	}
	
}
