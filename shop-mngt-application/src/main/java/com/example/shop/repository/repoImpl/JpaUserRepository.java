package com.example.shop.repository.repoImpl;

import com.example.shop.domain.model.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.entity.UserEntity;
import com.example.shop.mapper.UserMapper;
import com.example.shop.repository.jpa.SpringDataUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository springDataRepository;
    private final UserMapper userMapper;

    public JpaUserRepository(SpringDataUserRepository springDataRepository, UserMapper userMapper) {
        this.springDataRepository = springDataRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = springDataRepository.save(entity);
        return userMapper.toModel(saved);
    }

    @Override
    public User findById(String id) {
        UserEntity entity = springDataRepository.findById(id).orElse(null);
        return userMapper.toModel(entity);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity entity = springDataRepository.findByEmail(email);
        return userMapper.toModel(entity);
    }

    @Override
    public List<User> findAllUsers() {
        return springDataRepository.findAll().stream()
                .map(userMapper::toModel)
                .collect(Collectors.toList());
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