package com.tu.repository;

import static com.tu.util.Constants.FIND_RANDOM_GAME_QUERY;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tu.model.Game;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Integer>, JpaSpecificationExecutor<Game> {

	Game findById(int id);

	Game findByTitle(String title);

	@Query(value = FIND_RANDOM_GAME_QUERY, nativeQuery = true)
	public List<Game> pickRandom();

}
