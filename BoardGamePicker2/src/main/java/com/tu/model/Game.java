package com.tu.model;

import static com.tu.util.Messages.MAX_AGE_RESTRICTION;
import static com.tu.util.Messages.MAX_COMPLEXITY_RESTRICTION;
import static com.tu.util.Messages.MAX_PLAYERS_RESTRICTION;
import static com.tu.util.Messages.MIN_AGE_RESTRICTION;
import static com.tu.util.Messages.MIN_COMPLEXITY_RESTRICTION;
import static com.tu.util.Messages.MIN_PLAYERS_RESTRICTION;
import static com.tu.util.Messages.MISSING_CATEGORY;
import static com.tu.util.Messages.MISSING_COMPLEXITY;
import static com.tu.util.Messages.MISSING_DESCRIPTION;
import static com.tu.util.Messages.MISSING_MAX_PLAYERS;
import static com.tu.util.Messages.MISSING_MIN_AGE;
import static com.tu.util.Messages.MISSING_MIN_PLAYERS;
import static com.tu.util.Messages.MISSING_PLAYING_TIME;
import static com.tu.util.Messages.MISSING_TITLE;

import java.sql.Blob;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "games")
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "game_id")
	private int id;

	@Column(name = "title")
	@NotEmpty(message = MISSING_TITLE)
	private String title;

	@Column(name = "category")
	@NotEmpty(message = MISSING_CATEGORY)
	private String category;

	@Column(name = "min_players")
	@Min(value = 1, message = MIN_PLAYERS_RESTRICTION)
	@Max(value = 15, message = MIN_PLAYERS_RESTRICTION)
	@NotNull(message = MISSING_MIN_PLAYERS)
	private int minPlayers;

	@Column(name = "max_players")
	@Min(value = 2, message = MAX_PLAYERS_RESTRICTION)
	@Max(value = 20, message = MAX_PLAYERS_RESTRICTION)
	@NotNull(message = MISSING_MAX_PLAYERS)
	private int maxPlayers;

	@Column(name = "playing_time")
	@NotEmpty(message = MISSING_PLAYING_TIME)
	private String playingTime;

	@Column(name = "age")
	@Min(value = 1, message = MIN_AGE_RESTRICTION)
	@Max(value = 100, message = MAX_AGE_RESTRICTION)
	@NotNull(message = MISSING_MIN_AGE)
	private int appropriateForAge;

	@Column(name = "complexity")
	@Min(value = 1, message = MIN_COMPLEXITY_RESTRICTION)
	@Max(value = 100, message = MAX_COMPLEXITY_RESTRICTION)
	@NotNull(message = MISSING_COMPLEXITY)
	private int complexity;

	@Column(name = "description")
	@Length(max = 10000, min = 1)
	@NotEmpty(message = MISSING_DESCRIPTION)
	private String description;

	@Column(name = "image")
	private Blob image;

	@ManyToMany(mappedBy = "games")
	private List<User> users;

}
