package com.filecryptor.percistence;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "filesData")
public class FilesEntity {

    @Id
    private String id;
    private String title;
    private String dateReview;
    private Binary doc;
    private String username;
    private String secretKey;
    private String type;

/*    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void addUser(final UserEntity user) {
        this.user = user;
    }*/
}
