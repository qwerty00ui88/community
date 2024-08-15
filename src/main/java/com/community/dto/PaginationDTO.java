package com.community.dto;

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
public class PaginationDTO {
	private int currentPage;
	private int totalPages;
	private int startPage;
	private int endPage;
	private int pageSize;
	
    public PaginationDTO(int currentPage, int totalPages, int pageSize) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.startPage = Math.max(0, currentPage - 2);
        this.endPage = Math.min(totalPages - 1, currentPage + 2);
    }
}
