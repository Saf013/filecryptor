package com.filecryptor.percistence.repository;

import com.filecryptor.percistence.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntityRepository extends MongoRepository<UserEntity, String> {
    UserEntity findUserByUsername(String username);
}
