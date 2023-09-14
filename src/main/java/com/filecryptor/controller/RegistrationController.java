package com.filecryptor.controller;


import com.filecryptor.controller.dto.UserDto;
import com.filecryptor.percistence.UserEntity;
import com.filecryptor.service.DbClient;
import com.filecryptor.service.MongoAuthUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class RegistrationController {

    @Autowired
    private DbClient dbClient;

    @Autowired
    private MongoAuthUserDetailService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/registration")
    public String registration(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/register/save")
    public String addUser(@Valid @ModelAttribute("user") UserDto userDto,
                          BindingResult result, Model model) {
        UserEntity existingUser = dbClient.findByUserName(userDto.getUsername());
        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null,
                    "Уже є обліковий запис із тією самою електронною поштою");
        } else if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            String resultOfValidPassword = userService.getResultOfValidPassword(userDto.getPassword());
            if(!resultOfValidPassword.equals("ok")) {
                result.rejectValue("password", null, resultOfValidPassword);
            }
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/registration";
        }
        userService.saveUser(userDto);
        return "redirect:/registration?success";
    }

}
