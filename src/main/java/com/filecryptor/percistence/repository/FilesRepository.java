package com.filecryptor.percistence.repository;

import com.filecryptor.percistence.FilesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FilesRepository extends MongoRepository<FilesEntity, String> {

    FilesEntity findByUsername(String username);
    FilesEntity findAllById(String id);

    List<FilesEntity> findAllByUsername(String username);
}
