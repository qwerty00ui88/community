package com.community.category.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public List<Category> findByStatus(CategoryStatus status);

	public List<Category> findByStatusAndShowOnHome(CategoryStatus status, boolean showOnHome);

	public List<Category> findByStatusNot(CategoryStatus status);

	public Optional<Category> findByIdAndStatusNot(int id, CategoryStatus status);

	public Optional<Category> findByIdAndStatus(int id, CategoryStatus status);
}
