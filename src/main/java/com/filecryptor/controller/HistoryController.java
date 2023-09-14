package com.filecryptor.controller;

import com.filecryptor.percistence.FilesEntity;
import com.filecryptor.service.FileSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.ArrayList;
import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private FileSaveService fileSaveService;

    @GetMapping("/history")
    public String getHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<FilesEntity> allByUserName = fileSaveService.findAllByUserName(authentication.getName());
        List<FilesEntity> reverse = new ArrayList<>();
        for (int i = allByUserName.size() - 1; i >= 0; i--) {
            reverse.add(allByUserName.get(i));
        }
        model.addAttribute("documents", reverse);
        return "history";
    }
}
