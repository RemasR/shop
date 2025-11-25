package com.example.shop.repository.jpa;

import com.example.shop.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
