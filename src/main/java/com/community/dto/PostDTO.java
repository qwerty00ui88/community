package com.community.dto;

import java.util.Date;

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
public class PostDTO {
	private int id;
	private int categoryId;
	private int userId;
	private String title;
	private String contents;
	private int views;
//	private int fileId;
	private Date createdAt;
	private Date updatedAt;
}
