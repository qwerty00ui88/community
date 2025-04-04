package com.community.account.domain;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "account")
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private String name;

	private String nickname;

	private String password;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus status = AccountStatus.ACTIVE;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinTable(name = "account_roles", joinColumns = { @JoinColumn(name = "accountId") }, inverseJoinColumns = {
			@JoinColumn(name = "roleId") })
	@ToString.Exclude
	private Set<Role> accountRoles = new HashSet<>();

	@CreationTimestamp
	@Column(name = "createdAt", updatable = false)
	private ZonedDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updatedAt")
	private ZonedDateTime updatedAt;

	@PrePersist
	private void prePersist() {
		if (this.status == null) {
			this.status = AccountStatus.ACTIVE;
		}
	}
}
