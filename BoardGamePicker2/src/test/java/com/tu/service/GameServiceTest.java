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

import com.tu.model.Game;
import com.tu.model.GameSpecification;
import com.tu.model.Role;
import com.tu.model.User;
import com.tu.repository.GameRepository;
import com.tu.repository.UserRepository;

public class GameServiceTest {
	
	 @Mock
	    private UserRepository mockUserRepository;
	    @Mock
	    private GameRepository mockGameRepository;

	    private GameService gameServiceUnderTest;
	    private Game game;
	    private User user;
	    private Role userRole;
	    
	    @Before
	    public void setUp() {
	        initMocks(this);
	        gameServiceUnderTest = new GameService(mockGameRepository);
	 
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
	        
	        game = Game.builder()
	                .id(1)
	                .title("Title")
	                .category("Family")
	                .complexity(3)
	                .appropriateForAge(15)
	                .minPlayers(2)
	                .maxPlayers(6)
	                .description("This is the description")
	                .users(Arrays.asList(user))
	                .build();

	        Mockito.when(mockGameRepository.save(any())).thenReturn(game);
	        Mockito.doNothing().when(mockGameRepository).deleteById(any(Integer.class));
	        Mockito.when(mockGameRepository.findAll()).thenReturn(Arrays.asList(game));
	        Mockito.when(mockGameRepository.pickRandom()).thenReturn(Arrays.asList(game));
	        Mockito.when(mockGameRepository.findByTitle(anyString())).thenReturn(game);
	        
	        GameSpecification spec = new GameSpecification("Title", "category", 1, 2, 12, 5);
	        Mockito.when(mockGameRepository.findAll(spec)).thenReturn(Arrays.asList(game));
	        
	    }

	@Test
	public void testFindGameByTitle() {
		final String title = "Title";
		final Game result = gameServiceUnderTest.findByGameTitle(title);
		assertEquals(title, result.getTitle());
	}

	@Test
	public void testgetAllGames() {
		List<Game> result = gameServiceUnderTest.getAllGames();
		assertEquals(1, result.size());
	}

	@Test
	public void testSaveGame() {
		final String title = "Title";
		Game result = gameServiceUnderTest.saveGame(Game.builder().build());
		assertEquals(title, result.getTitle());
	}

	@Test
	public void testDeleteGame() {
		gameServiceUnderTest.deleteGame(1);
		assertEquals(1, gameServiceUnderTest.getAllGames().size());
	}

	@Test
	public void testFindRandomGame() {
		List<Game> result = gameServiceUnderTest.findRandomGame();
		assertEquals(1, result.size());
	}

}
