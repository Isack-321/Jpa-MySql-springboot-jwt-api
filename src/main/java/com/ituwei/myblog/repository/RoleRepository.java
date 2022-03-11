package com.ituwei.myblog.repository;

import com.ituwei.myblog.entity.role.Role;
import com.ituwei.myblog.entity.role.Rolename;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(Rolename rolename);
}
