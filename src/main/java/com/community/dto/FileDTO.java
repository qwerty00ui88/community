package com.community.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
	private int id;
	private String url;
	private String domain;
	private Integer domainId;
	private String type;
	private Long size;
	private Map<String, String> metadata;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
}
