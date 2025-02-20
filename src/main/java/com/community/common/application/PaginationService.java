package com.community.common.application;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.community.common.presentation.dto.PaginationDto;

@Service
public class PaginationService {

	public <T> PaginationDto getPaginationDetails(Page<T> page) {
		return new PaginationDto(page.getNumber(), page.getTotalPages(), page.getSize());
	}

}
