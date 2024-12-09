package com.community.entity;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.community.enums.CommentStatus;

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
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
@Table(name = "comment")
@Entity
public class CommentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "postId")
	private int postId;

	@Column(name = "userId")
	private int userId;

	@Column(name = "parentId")
	private Integer parentId;

	private String contents;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private CommentStatus status = CommentStatus.DEFAULT;

	@CreationTimestamp
	@Column(name = "createdAt", updatable = false)
	private ZonedDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updatedAt")
	private ZonedDateTime updatedAt;
}
