package com.tu.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tu.model.Game;
import com.tu.repository.GameRepository;

@Service("gameService")
public class GameService {

	private GameRepository gameRepository;

	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public List<Game> getAllGames() {
		return gameRepository.findAll();
	}

	public Game findByGameTitle(String title) {
		return gameRepository.findByTitle(title);
	}

	public Game saveGame(@Valid Game game) {
		return gameRepository.save(game);

	}

	public void deleteGame(int id) {
		gameRepository.deleteById(id);
	}

	public List<Game> findRandomGame() {
		return gameRepository.pickRandom();
	}

	public List<Game> applyFilter(Specification<Game> spec) {
		return gameRepository.findAll(spec);
	}

	public Game findByGameId(int id) {
		return gameRepository.findById(id);
	}
}
