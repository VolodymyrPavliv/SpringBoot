package com.mushroom.novoselytsia.repo;

import com.mushroom.novoselytsia.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername (String username);
}
