package com.tu.controller;

import static com.tu.util.Constants.ADMIN_GAME_EDIT_VIEW;
import static com.tu.util.Constants.ADMIN_GAME_VIEW;
import static com.tu.util.Constants.ADMIN_HOME_VIEW;
import static com.tu.util.Constants.ADMIN_USERS_VIEW;
import static com.tu.util.Constants.GAMES_WITH_IMAGES_OBJECT;
import static com.tu.util.Constants.GAME_OBJECT;
import static com.tu.util.Constants.USERNAME_OBJECT;
import static com.tu.util.Constants.USERS_OBJECT;
import static com.tu.util.Messages.GAME_ALREADY_EXISTS;
import static com.tu.util.Messages.SUCCESSFUL_ADDING_OF_GAME;
import static com.tu.util.Messages.SUCCESSFUL_EDITING_OF_GAME;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tu.model.Game;
import com.tu.model.User;
import com.tu.service.GameService;
import com.tu.service.UserService;

@Controller
public class AdminPanelController {

	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public ModelAndView adminHome() throws SQLException {
		ModelAndView modelAndView = new ModelAndView();
		User user = findUserByUsername();
		List<Game> games = gameService.getAllGames();
		Map<String, Game> gamesWithImages = getGamesWithImages(games);
		modelAndView.addObject(USERNAME_OBJECT, "Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
		modelAndView.addObject(GAMES_WITH_IMAGES_OBJECT, gamesWithImages);
		modelAndView.setViewName(ADMIN_HOME_VIEW);
		return modelAndView;
	}

	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public ModelAndView adminUsers() {
		ModelAndView modelAndView = new ModelAndView();
		addAllUsersToTheView(modelAndView);
		return modelAndView;
	}

	@RequestMapping(value = "/admin/game", method = RequestMethod.GET)
	public ModelAndView adminAddGame() {
		ModelAndView modelAndView = new ModelAndView();
		Game game = new Game();

		modelAndView.addObject(GAME_OBJECT, game);
		modelAndView.setViewName(ADMIN_GAME_VIEW);
		return modelAndView;
	}

	@RequestMapping(value = "/admin/game/edit", method = RequestMethod.GET)
	public ModelAndView adminEditGame(@RequestParam(value = "title", required = true) String title) {
		ModelAndView modelAndView = new ModelAndView();
		Game game = gameService.findByGameTitle(title);

		modelAndView.addObject(GAME_OBJECT, game);
		modelAndView.setViewName(ADMIN_GAME_EDIT_VIEW);
		return modelAndView;
	}

	@RequestMapping(value = "/admin/game/delete", method = RequestMethod.POST)
	public ModelAndView adminDeteleGame(@RequestParam(value = "id", required = true) int id) throws SQLException {
		ModelAndView modelAndView = new ModelAndView();
		gameService.deleteGame(id);
		addAllGamesToTheView(modelAndView);

		return modelAndView;
	}

	@RequestMapping(value = "/admin/user/add", method = RequestMethod.POST)
	public ModelAndView addAdminPrivileges(@RequestParam(value = "user", required = true) String username) {
		ModelAndView modelAndView = new ModelAndView();
		User user = userService.findUserByUsername(username);
		user.setId(user.getId());
		userService.saveUser(user, "ADMIN");
		addAllUsersToTheView(modelAndView);
		return modelAndView;
	}

	@RequestMapping(value = "/admin/user/remove", method = RequestMethod.POST)
	public ModelAndView removeAdminPrivileges(@RequestParam(value = "user", required = true) String username) {
		ModelAndView modelAndView = new ModelAndView();
		User user = userService.findUserByUsername(username);
		user.setId(user.getId());
		userService.saveUser(user, "USER");
		addAllUsersToTheView(modelAndView);
		return modelAndView;
	}

	@RequestMapping(value = "/admin/game/edit", method = RequestMethod.POST, consumes = "multipart/form-data")
	public ModelAndView adminEditedGame(@Valid Game game, BindingResult bindingResult,
			@RequestParam("image") MultipartFile file, @RequestParam("id") int id)
			throws IOException, SerialException, SQLException {
		ModelAndView modelAndView = new ModelAndView();

		if (checkForErrors(bindingResult)) {
			modelAndView.setViewName(ADMIN_HOME_VIEW);
		} else {
			Blob blob = new javax.sql.rowset.serial.SerialBlob(file.getBytes());
			game.setId(id);
			game.setImage(blob);
			gameService.saveGame(game);

			modelAndView.addObject("successMessage", SUCCESSFUL_EDITING_OF_GAME);
			addAllGamesToTheView(modelAndView);
		}

		return modelAndView;
	}

	@RequestMapping(value = "/admin/game", method = RequestMethod.POST, consumes = "multipart/form-data")
	public ModelAndView createNewGame(@Valid Game game, BindingResult bindingResult,
			@RequestParam("image") MultipartFile file) throws SerialException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		Game gameExists = gameService.findByGameTitle(game.getTitle());

		if (gameExists != null) {
			bindingResult.rejectValue("title", "error.game", GAME_ALREADY_EXISTS);
		}

		if (checkForErrors(bindingResult)) {
			modelAndView.setViewName(ADMIN_GAME_VIEW);
		} else {
			Blob blob = new javax.sql.rowset.serial.SerialBlob(file.getBytes());
			game.setImage(blob);
			gameService.saveGame(game);
			modelAndView.addObject("successMessage", SUCCESSFUL_ADDING_OF_GAME);
			addAllGamesToTheView(modelAndView);

		}
		return modelAndView;
	}

//	private ModelAndView assembleView(String viewName, Map<String, Object> objects) {
//		ModelAndView modelAndView = new ModelAndView();
//		objects.forEach((k, v) -> {
//			modelAndView.addObject(k, v);
//		});
//		modelAndView.setViewName(viewName);
//		
//		return modelAndView;
//	}

	private void addAllGamesToTheView(ModelAndView modelAndView) throws SQLException {
		List<Game> games = gameService.getAllGames();
		Map<String, Game> gamesWithImages = getGamesWithImages(games);
		modelAndView.addObject(GAMES_WITH_IMAGES_OBJECT, gamesWithImages);
		modelAndView.setViewName(ADMIN_HOME_VIEW);
	}

	private void addAllUsersToTheView(ModelAndView modelAndView) {
		List<User> users = userService.getAllUsers();
		modelAndView.addObject(USERS_OBJECT, users);
		modelAndView.setViewName(ADMIN_USERS_VIEW);
	}

	private User findUserByUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUsername(auth.getName());
		return user;
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

	private boolean checkForErrors(BindingResult bindingResult) {
		return bindingResult.hasFieldErrors("title") || bindingResult.hasFieldErrors("age")
				|| bindingResult.hasFieldErrors("category") || bindingResult.hasFieldErrors("description")
				|| bindingResult.hasFieldErrors("complexity") || bindingResult.hasFieldErrors("max_players")
				|| bindingResult.hasFieldErrors("min_players") || bindingResult.hasFieldErrors("playing_time");
	}
}
