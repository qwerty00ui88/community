package com.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.community.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {

	public List<FileEntity> findByDomainAndDomainId(String domain, int domainId);

}
