package com.filecryptor.service;

import com.filecryptor.percistence.UserEntity;
import com.filecryptor.percistence.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbClient {

    @Autowired
    private UserEntityRepository userEntityRepository;

    public UserEntity findByUserName(String userName) {
        return userEntityRepository.findUserByUsername(userName);
    }

    public void createUser(UserEntity user) {
        userEntityRepository.save(user);
    }
}
