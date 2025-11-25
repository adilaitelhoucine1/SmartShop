package org.smartshop.smartshop.repository;

import org.smartshop.smartshop.entity.User;
import org.smartshop.smartshop.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByRole(UserRole role);
    User findByUsername(String username);
}