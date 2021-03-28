package com.se2.hanuairline.repository.user;

import com.se2.hanuairline.model.user.Role;
import com.se2.hanuairline.model.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);


}