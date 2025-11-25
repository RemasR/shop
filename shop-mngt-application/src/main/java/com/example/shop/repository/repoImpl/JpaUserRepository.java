package com.example.shop.repository.repoImpl;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.repository.jpa.SpringDataUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository springDataRepository;

    public JpaUserRepository(SpringDataUserRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public User save(User user) {
        return springDataRepository.save(user);
    }

    @Override
    public User findById(String id) {
        return springDataRepository.findById(id).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return springDataRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return springDataRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return springDataRepository.existsById(id);
    }
}