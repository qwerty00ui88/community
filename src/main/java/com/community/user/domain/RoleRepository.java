package com.community.user.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String name);

	@Override
	void delete(Role role);

	@Query("select r from Role r where r.isExpression = 'N'")
	List<Role> findAllRolesWithoutExpression();
}
