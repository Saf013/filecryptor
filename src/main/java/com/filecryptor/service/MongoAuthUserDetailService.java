package com.filecryptor.service;

import com.filecryptor.controller.dto.UserDto;
import com.filecryptor.percistence.Role;
import com.filecryptor.percistence.UserEntity;
import com.filecryptor.percistence.repository.UserEntityRepository;
import org.modelmapper.ModelMapper;
import org.passay.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

@Service
public class MongoAuthUserDetailService implements UserDetailsService {

    private DbClient dbClient;

    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    public MongoAuthUserDetailService(DbClient dbClient, ModelMapper modelMapper, @Lazy PasswordEncoder passwordEncoder) {
        this.dbClient = dbClient;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return dbClient.findByUserName(username);
    }

    public void saveUser(UserDto userDto) {
        UserEntity user = modelMapper.map(userDto, UserEntity.class);
        user.setUsername(userDto.getUsername());
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRoles(Collections.singleton(Role.USER));
        user.setActive(true);
        //user.setDateOfCreated(ServiceUtility.dataFormat(Instant.now()));
        dbClient.createUser(user);
    }

    public String getResultOfValidPassword(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new AllowedRegexRule("[a-zA-Z]"),
                //new NumericalSequenceRule(3,false),
                //new AlphabeticalSequenceRule(3,false),
                //new QwertySequenceRule(3,false),
                new WhitespaceRule()));

        RuleResult validate = validator.validate(new PasswordData(password));
        if(validate.isValid()) {
            return "ok";
        }

        return validator.getMessages(validate).toString();
    }
}
