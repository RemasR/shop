package com.example.shop.repository.jpa;

import com.example.shop.domain.model.User;
import com.example.shop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
}
