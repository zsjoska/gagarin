package org.csovessoft.contabil.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.internal.NotNull;

@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

	private static final long serialVersionUID = 4384614532696714328L;

	private String username;
	private String password;
	private String name;

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(nullable = false, unique = true)
	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Id
	@NotNull
	public long getId() {
		return super.getId();
	}
}
