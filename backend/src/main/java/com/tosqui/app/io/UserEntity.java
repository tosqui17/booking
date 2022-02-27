package com.tosqui.app.io;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(nullable = false, length = 16, unique = true)
	@Size(min=4,max=16)
	private String username;
	@Column(nullable = false, length = 32)
	@Size(min=4,max=32)
	private String firstName;
	@Column(nullable = false, length = 32)
	@Size(min=4,max=32)
	private String lastName;
	@Column(nullable = false, length = 64, unique = true)
	@Size(min=4,max=64)
	private String email;
	@Column(nullable = false)
	private String encryptedPassword;
	@Column(nullable = false)
	private String verificationToken;
	@Column(nullable = false)
	private boolean verified = false; // can be shared across different db format
	@Column(nullable = false)
	private boolean accountNonLocked = true;
	@Column
	private int failedLoginAttempts = 0;
	@Version
	private long version;
	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "roles_id")
	private List<RoleEntity> roles = new ArrayList<RoleEntity>();
	private boolean softDeleted = false;

	public boolean isSoftDeleted() {
		return this.softDeleted;
	}

	public void setSoftDeleted(boolean softDeleted) {
		this.softDeleted = softDeleted;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean getSoftDeleted() {
		return this.softDeleted;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncryptedPassword() {
		return this.encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getVerificationToken() {
		return this.verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

	public boolean isVerified() {
		return this.verified;
	}

	public boolean getVerified() {
		return this.verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public boolean getAccountNonLocked() {
		return this.accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public int getFailedLoginAttempts() {
		return this.failedLoginAttempts;
	}

	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public long getVersion() {
		return this.version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<RoleEntity> getRoles() {
		return this.roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

}