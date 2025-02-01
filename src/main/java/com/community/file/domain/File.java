package com.community.file.domain;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.community.file.application.dto.MetadataDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "file")
@Entity
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private String url;

	private String domain;

	@Column(name = "domainId")
	private Integer domainId;

	private String type;

	private Long size;

	@Column(columnDefinition = "json")
	private String metadata;

	@CreationTimestamp
	@Column(name = "createdAt", updatable = false)
	private ZonedDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updatedAt")
	private ZonedDateTime updatedAt;

	@Transient
	private MetadataDto metadataObject;

	// 데이터 로드 시 JSON -> 객체 변환
	@PostLoad
	private void convertMetadataToObject() {
		if (this.metadata != null) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				this.metadataObject = objectMapper.readValue(this.metadata, MetadataDto.class);
			} catch (Exception e) {
				e.printStackTrace();
				this.metadataObject = null;
			}
		}
	}

	// 데이터 저장 전 객체 -> JSON 변환
	@PrePersist
	@PreUpdate
	private void convertObjectToMetadata() {
		if (this.metadataObject != null) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				this.metadata = objectMapper.writeValueAsString(this.metadataObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
