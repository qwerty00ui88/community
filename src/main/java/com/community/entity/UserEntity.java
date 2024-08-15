package com.community.entity;

import java.time.ZonedDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.community.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "user")
@Entity
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String nickname;

	private String password;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private UserStatus status = UserStatus.USER;

	@UpdateTimestamp
	@Column(name = "createdAt")
	private ZonedDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updatedAt")
	private ZonedDateTime updatedAt;
}


