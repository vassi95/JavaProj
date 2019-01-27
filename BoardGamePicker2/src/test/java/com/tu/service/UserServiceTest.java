package com.tu.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tu.model.Role;
import com.tu.model.User;
import com.tu.repository.RoleRepository;
import com.tu.repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;

    private UserService userServiceUnderTest;
    private User user;
    private Role userRole;

    @Before
    public void setUp() {
        initMocks(this);
        userServiceUnderTest = new UserService(mockUserRepository, mockRoleRepository, mockBCryptPasswordEncoder);
        userRole = Role.builder()
        			.id(1)
        			.role("USER")
        			.build();

        
        user = User.builder()
                .id(1)
                .firstName("Ivan")
                .lastName("Ivanov")
                .username("ivan123")
                .email("ivan@gmail.com")
                .roles(Arrays.asList(userRole))
                .build();

        Mockito.when(mockUserRepository.save(any())).thenReturn(user);
        Mockito.when(mockUserRepository.findAll()).thenReturn(Arrays.asList(user));
        Mockito.when(mockUserRepository.findByUsername(anyString())).thenReturn(user);
    }

	@Test
	public void testFindUserByUsername() {
		final String username = "ivan123";
		final User result = userServiceUnderTest.findUserByUsername(username);
		assertEquals(username, result.getUsername());
	}

	@Test
	public void testSaveUser() {
		final String email = "ivan@gmail.com";
		User result = userServiceUnderTest.saveUser(User.builder().build(), false);
		assertEquals(email, result.getEmail());
	}
	
	@Test
	public void testUpdateUser() {
		User result = userServiceUnderTest.saveUser(User.builder().build(), "USER");
		assertEquals(1, result.getId());
	}

	@Test
	public void testgetAllUsers() {
		List<User> result = userServiceUnderTest.getAllUsers();
		assertEquals(1, result.size());

	}

}
