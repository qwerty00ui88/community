package com.community.file.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
	public List<File> findByDomainAndDomainId(String domain, int domainId);
}
