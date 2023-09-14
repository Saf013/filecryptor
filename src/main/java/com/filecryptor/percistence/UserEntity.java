package com.filecryptor.percistence;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "userData")
public class UserEntity implements UserDetails {

    private @MongoId ObjectId id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
    private Set<Role> userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getUserRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
/*    @OneToMany(mappedBy = "userEntity")
    private List<FilesEntity> files = new ArrayList<>();

    public void addJourney(final FilesEntity filesEntity) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(filesEntity);
        filesEntity.addUser(this);
    }*/
}
