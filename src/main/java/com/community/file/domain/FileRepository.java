package com.community.file.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
	public List<FileEntity> findByDomainAndDomainId(String domain, int domainId);
}
