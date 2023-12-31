package com.sth.userservice.repository;

import com.sth.userservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public User findByEmail(String email);

    public User findByUsername(String username);

    public boolean existsUserById(String id);
}
