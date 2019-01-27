package com.tu.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class GameSpecification implements Specification<Game> {

	private static final long serialVersionUID = 1L;
	private final String title;
	private final String category;
	private int minPlayers;
	private final int maxPlayers;
	private final int appropriateForAge;
	private final int complexity;

	public GameSpecification(String title, String category, int minPlayers, int maxPlayers, int appropriateForAge,
			int complexity) {
		this.title = title;
		this.category = category;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.appropriateForAge = appropriateForAge;
		this.complexity = complexity;
	}

	public Predicate toPredicate(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Predicate predicate = builder.conjunction();

		List<Expression<Boolean>> expressions = new ArrayList<Expression<Boolean>>();

		if (title != null && !title.isEmpty()) {
			expressions.add(builder.like(root.get("title"), title));
		}

		if (category != null && !category.isEmpty()) {
			expressions.add(builder.like(root.get("category"), category));
		}

		if (minPlayers > 0) {
			expressions.add(builder.equal(root.get("minPlayers"), minPlayers));
		}

		if (maxPlayers > 0) {
			expressions.add(builder.equal(root.get("maxPlayers"), maxPlayers));
		}

		if (appropriateForAge > 0) {
			expressions.add(builder.equal(root.get("appropriateForAge"), appropriateForAge));
		}

		if (complexity > 0) {
			expressions.add(builder.equal(root.get("complexity"), complexity));
		}

		predicate.getExpressions().addAll(expressions);
		return predicate;
	}
}
