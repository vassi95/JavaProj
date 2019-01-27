package com.tu.util;

public class Constants {
	
	//VIEWS
	public static final String REGISTRATION_VIEW = "registration";
	public static final String LOGIN_VIEW = "login";
	public static final String USER_HOME_VIEW = "user/home";
	public static final String USER_GAME_VIEW = "user/game";
	public static final String USER_PROFILE_VIEW = "user/profile";
	public static final String USER_FILTER_VIEW = "user/filter";
	public static final String USER_PICKED_GAMES_VIEW = "user/pickedGames";
	public static final String ADMIN_HOME_VIEW = "admin/home";
	public static final String ADMIN_USERS_VIEW = "admin/users";
	public static final String ADMIN_GAME_VIEW = "admin/game";
	public static final String ADMIN_GAME_EDIT_VIEW = "admin/gameedit";
	
	//OBJECTS FOR VIEWS
	public static final String USER_OBJECT = "user";
	public static final String USERS_OBJECT = "users";
	public static final String USERNAME_OBJECT = "userName";
	public static final String GAMES_OBJECT = "games";
	public static final String GAME_OBJECT = "game";
	public static final String GAMES_WITH_IMAGES_OBJECT = "gamesWithImages";
	
	public static final String FIND_RANDOM_GAME_QUERY = "select * from games ORDER BY rand() LIMIT 1";
}
