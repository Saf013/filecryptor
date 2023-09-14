package com.filecryptor.service;

import com.filecryptor.percistence.FilesEntity;
import com.filecryptor.percistence.repository.FilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class FileSaveService {

    @Autowired
    private FilesRepository filesRepository;

    public String saveFile(String title, String username, Binary file, String key, String type) {
        FilesEntity filesEntity = new FilesEntity();
        filesEntity.setTitle(title);
        filesEntity.setUsername(username);
        filesEntity.setSecretKey(key);
        filesEntity.setType(type);
        filesEntity.setDateReview(ServiceUtility.dataFormat(Instant.now()));
        filesEntity.setDoc(new Binary(BsonBinarySubType.BINARY, file.getData()));
        filesEntity = filesRepository.insert(filesEntity);
        return filesEntity.getId();
    }

    public List<FilesEntity> findAllByUserName(String username) {
        return filesRepository.findAllByUsername(username);
    }

    public FilesEntity findByFileByUsername(String userName) {
        return filesRepository.findByUsername(userName);
    }

    public FilesEntity findById(String id) {
        return filesRepository.findAllById(id);
    }
}
