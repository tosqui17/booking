package com.tosqui.app.io;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity(name = "roles")
public class RoleEntity {

	@Id
	@GeneratedValue
	private Long id;
	private SimpleGrantedAuthority role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SimpleGrantedAuthority getRole() {
		return role;
	}

	public void setRole(SimpleGrantedAuthority role) {
		this.role = role;
	}

}
