package com.community.file.application.dto;

import java.time.ZonedDateTime;

import com.community.file.domain.File;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
	private int id;
	private String url;
	private String domain;
	private Integer domainId;
	private String type;
	private Long size;
	private String metadata;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;

	public File toEntity() {
		return File.builder().domain(domain).domainId(domainId).type(type).size(size).metadata(metadata).build();
	}
}
