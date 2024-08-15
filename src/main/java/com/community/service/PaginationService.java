package com.community.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.community.dto.PaginationDTO;

@Service
public class PaginationService {

	public <T> PaginationDTO getPaginationDetails(Page<T> page) {
		return new PaginationDTO(page.getNumber(), page.getTotalPages(), page.getSize());
	}

}
