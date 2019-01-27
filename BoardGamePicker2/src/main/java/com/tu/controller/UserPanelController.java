package com.tu.controller;

import static com.tu.util.Constants.GAMES_OBJECT;
import static com.tu.util.Constants.GAMES_WITH_IMAGES_OBJECT;
import static com.tu.util.Constants.GAME_OBJECT;
import static com.tu.util.Constants.USERNAME_OBJECT;
import static com.tu.util.Constants.USER_FILTER_VIEW;
import static com.tu.util.Constants.USER_GAME_VIEW;
import static com.tu.util.Constants.USER_HOME_VIEW;
import static com.tu.util.Constants.USER_OBJECT;
import static com.tu.util.Constants.USER_PICKED_GAMES_VIEW;
import static com.tu.util.Constants.USER_PROFILE_VIEW;

import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tu.model.Game;
import com.tu.model.GameSpecification;
import com.tu.model.User;
import com.tu.service.GameService;
import com.tu.service.UserService;

@Controller
public class UserPanelController {

	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/user/home", method = RequestMethod.GET)
	public ModelAndView userHome() {

		User user = findUserByUsername();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(USERNAME_OBJECT, "Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
		modelAndView.addObject(USER_OBJECT, user);
		modelAndView.setViewName(USER_HOME_VIEW);

		return modelAndView;
	}

	@RequestMapping(value = "/user/profile", method = RequestMethod.GET)
	public ModelAndView showUserProfile() {

		User user = findUserByUsername();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(USER_OBJECT, user);
		modelAndView.addObject(GAMES_OBJECT, user.getGames());
		modelAndView.setViewName(USER_PROFILE_VIEW);

		return modelAndView;
	}

	@RequestMapping(value = "/user/game/show", method = RequestMethod.GET)
	public ModelAndView showGameInfo(@RequestParam("title") String title) {

		Game game = gameService.findByGameTitle(title);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(GAME_OBJECT, game);
		modelAndView.setViewName(USER_GAME_VIEW);

		return modelAndView;
	}

	@RequestMapping(value = "/user/game/remove", method = RequestMethod.POST)
	public ModelAndView removeGameFromFavourites(@RequestParam("title") String title, @RequestParam("id") int id) {

		User user = findUserByUsername();
		Game game = gameService.findByGameTitle(title);
		ModelAndView modelAndView = new ModelAndView();
		removeGameFromUserGames(user, id, game);
		modelAndView.addObject(USER_OBJECT, user);
		modelAndView.setViewName(USER_PROFILE_VIEW);

		return modelAndView;
	}

	@RequestMapping(value = "/user/pickrandom", method = RequestMethod.GET)
	public ModelAndView pickRandomGame() throws SQLException {

		User user = findUserByUsername();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(USER_OBJECT, user);
		modelAndView.setViewName(USER_PICKED_GAMES_VIEW);

		List<Game> games = gameService.findRandomGame();
		Map<String, Game> gamesWithImages = getGamesWithImages(games);
		modelAndView.addObject(GAMES_WITH_IMAGES_OBJECT, gamesWithImages);
		return modelAndView;
	}

	@RequestMapping(value = "/user/game/addFav", method = RequestMethod.POST)
	public ModelAndView addGameToFavourites(@RequestParam("title") String title, @RequestParam("id") int id) {

		Game game = gameService.findByGameTitle(title);
		User user = findUserByUsername();
		ModelAndView modelAndView = new ModelAndView();
		addGameToUserGames(id, game, user);
		modelAndView.addObject(USER_OBJECT, user);
		modelAndView.setViewName(USER_HOME_VIEW);

		return modelAndView;
	}

	@RequestMapping(value = "/user/filtergames", method = RequestMethod.GET)
	public ModelAndView filterOutGames() {

		User user = findUserByUsername();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(USER_OBJECT, user);
		modelAndView.setViewName(USER_FILTER_VIEW);

		return modelAndView;
	}

	@RequestMapping(value = "/user/showfiltered", method = RequestMethod.POST)
	public ModelAndView searchForGames(@RequestParam("title") String title, @RequestParam("category") String category,
			@RequestParam(name = "minPlayers", defaultValue = "0") Integer minPlayers,
			@RequestParam(name = "maxPlayers", defaultValue = "0") Integer maxPlayers,
			@RequestParam(name = "appropriateForAge", defaultValue = "0") Integer appropriateForAge,
			@RequestParam(name = "complexity", defaultValue = "0") Integer complexity) throws SQLException {

		User user = findUserByUsername();
		ModelAndView modelAndView = new ModelAndView();
		Specification<Game> spec = new GameSpecification(title, category, minPlayers, maxPlayers, appropriateForAge,
				complexity);
		List<Game> games = gameService.applyFilter(spec);
		Map<String, Game> gamesWithImages = getGamesWithImages(games);
		modelAndView.addObject(GAMES_WITH_IMAGES_OBJECT, gamesWithImages);
		modelAndView.addObject(USER_OBJECT, user);
		modelAndView.setViewName(USER_PICKED_GAMES_VIEW);

		return modelAndView;
	}

	private User findUserByUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUsername(auth.getName());
		return user;
	}

	private void removeGameFromUserGames(User user, int id, Game game) {
		List<Game> oldGames = user.getGames();
		user.setId(id);
		oldGames.remove(game);
		user.setGames(oldGames);
		userService.saveUser(user, true);
	}

	private void addGameToUserGames(int id, Game game, User user) {
		List<Game> newGames = user.getGames();
		user.setId(id);
		newGames.add(game);
		user.setGames(newGames);
		userService.saveUser(user, true);
	}

	private Map<String, Game> getGamesWithImages(List<Game> games) throws SQLException {
		Map<String, Game> gamesWithImages = new HashMap<String, Game>();
		for (Game game : games) {
			String base64Image = Base64.getEncoder()
					.encodeToString(game.getImage().getBytes(1, (int) game.getImage().length()));
			gamesWithImages.put(base64Image, game);
		}
		return gamesWithImages;
	}

}
