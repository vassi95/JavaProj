package com.tu.model;

import static com.tu.util.Messages.EMAIL_RESTRICTION;
import static com.tu.util.Messages.MISSING_EMAIL;
import static com.tu.util.Messages.MISSING_FIRST_NAME;
import static com.tu.util.Messages.MISSING_LAST_NAME;
import static com.tu.util.Messages.MISSING_PASSWORD;
import static com.tu.util.Messages.MISSING_USERNAME;
import static com.tu.util.Messages.PASSWORD_RESTRICTION;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private int id;

	@Column(name = "first_name")
	@NotEmpty(message = MISSING_FIRST_NAME)
	private String firstName;

	@Column(name = "last_name")
	@NotEmpty(message = MISSING_LAST_NAME)
	private String lastName;

	@Column(name = "username")
	@NotEmpty(message = MISSING_USERNAME)
	private String username;

	@Column(name = "email")
	@Email(message = EMAIL_RESTRICTION)
	@NotEmpty(message = MISSING_EMAIL)
	private String email;

	@Column(name = "user_password")
	@Length(min = 5, message = PASSWORD_RESTRICTION)
	@NotEmpty(message = MISSING_PASSWORD)
	private String password;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "users_games", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
	private List<Game> games;

}
