package com.tu.controller;

import static com.tu.util.Messages.*;
import static com.tu.util.Constants.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tu.model.User;
import com.tu.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(LOGIN_VIEW);
		
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(USER_OBJECT, new User());
		modelAndView.setViewName(REGISTRATION_VIEW);
		
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByUsername(user.getUsername());

		if (userExists != null) {
			bindingResult.rejectValue("username", "error.user", USER_ALREADY_EXISTS);
		}
		
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName(REGISTRATION_VIEW);
		} else {
			userService.saveUser(user, false);
			modelAndView.addObject("successMessage", SUCCESSFUL_REGISTRATION_MESSAGE);
			modelAndView.addObject(USER_OBJECT, new User());
			modelAndView.setViewName(REGISTRATION_VIEW);

		}
		return modelAndView;
	}

}
