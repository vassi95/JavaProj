package com.tu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tu.model.Role;
import com.tu.model.User;
import com.tu.repository.RoleRepository;
import com.tu.repository.UserRepository;

@Service("userService")
public class UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User saveUser(User user, boolean isUserExisting) {
		if (!isUserExisting) {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		}
		Role userRole = roleRepository.findByRole("USER");
		user.setRoles(new ArrayList<Role>(Arrays.asList(userRole)));
		return userRepository.save(user);
	}

	public User saveUser(User user, String role) {
		Role userRole = roleRepository.findByRole(role);
		user.setRoles(new ArrayList<Role>(Arrays.asList(userRole)));
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

}