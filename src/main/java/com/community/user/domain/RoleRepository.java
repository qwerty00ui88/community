package com.community.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String name);

	@Override
	void delete(Role role);

}
