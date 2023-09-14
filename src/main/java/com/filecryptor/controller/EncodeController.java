package com.filecryptor.controller;

import com.filecryptor.controller.dto.FileDto;
import com.filecryptor.controller.dto.UserDto;
import com.filecryptor.percistence.FilesEntity;
import com.filecryptor.service.CryptoService;
import com.filecryptor.service.FileSaveService;
import lombok.SneakyThrows;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.bouncycastle.jcajce.provider.symmetric.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.io.File;
import java.util.Base64;
import java.util.Objects;

@Controller
public class EncodeController {

    @Autowired
    private FileSaveService fileSaveService;

    @Autowired
    private CryptoService cryptoService;

    @GetMapping("/encode")
    public String getEncodePage(Model model) {
        FileDto fileDto = new FileDto();
        model.addAttribute("document", fileDto);
        return "encode";
    }

    @SneakyThrows
    @PostMapping("/encode/encoding")
    public String encoding(@Valid @ModelAttribute("document") FileDto fileDto, @RequestParam(name = "type") String type, @RequestParam("secretKey") String key,
                           @RequestParam(name = "file")MultipartFile file, @RequestParam("mode") String mode, BindingResult result, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String fileName = "";
        if (mode.equals("encod")) {
            fileName = split[0]+"-"+type+"."+split[1];
        } else {
            fileName = split[0]+"-"+type+"-decod."+split[1];
        }
        String id = "";

        if(fileDto.getSecretKey().isEmpty()) {
            result.rejectValue("secretKey", null, "Це поле не може бути порожнiм!");
        } else if (type.equals("DES") && !cryptoService.validateDesKey(fileDto.getSecretKey()).equals("valid")){
            result.rejectValue("secretKey", null, "Неправильний ключ");
        } else if (type.equals("AES") && !cryptoService.isValidAESKeyString(key)) {
            result.rejectValue("secretKey", null, "Неправильний ключ");
        } else {
            if(fileDto.getType().equals("DES")) {
                type = type.concat("ede");
            }

            int mod = 0;
            if(mode.equals("encod")) {
                mod = 1;
            } else {
                mod = 2;
            }
            id = fileSaveService.saveFile(fileName, currentPrincipalName, cryptoService.encryptToBinary(type, file, key, mod), key, type);
        }


        if(result.hasErrors()) {
            model.addAttribute("document", fileDto);
            return "/encode";
        }
        return "redirect:/encods/" + id;
    }

    @GetMapping("/encods/{id}")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable String id, Model model) {
        FilesEntity doc = fileSaveService.findById(id);


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.TEXT_PLAIN_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getTitle() + "\"")
                .body(new ByteArrayResource(doc.getDoc().getData()));
    }
}
