package com.community.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.community.entity.CategoryEntity;
import com.community.enums.CategoryStatus;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

	public List<CategoryEntity> findByStatus(CategoryStatus status);

	public List<CategoryEntity> findByStatusAndShowOnHome(CategoryStatus status, boolean showOnHome);

	public List<CategoryEntity> findByStatusNot(CategoryStatus status);
	
	public Optional<CategoryEntity> findByIdAndStatusNot(int id, CategoryStatus status);
}
