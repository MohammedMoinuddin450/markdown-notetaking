package com.eb2.markdownnotetakingapp.controller;

import com.eb2.markdownnotetakingapp.exception.InvalidFile;
import com.eb2.markdownnotetakingapp.service.FileService;
import com.eb2.markdownnotetakingapp.service.StorageService;
import com.eb2.markdownnotetakingapp.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final StorageService storageService;
    private final FileService fileService;

    public FileController(StorageService storageService, FileService fileService) {
        this.storageService = storageService;
        this.fileService = fileService;
    }

    @PostMapping("/check")
    public List<Response> checkGrammar(@RequestParam("file") MultipartFile file) {

        if (!storageService.isValidExtension(file)) {
            throw new InvalidFile("Invalid file extension");
        }

        return fileService.checkGrammar(file);

    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> save(@RequestParam("file") MultipartFile file) {

        if (!storageService.isValidExtension(file)) {
            throw new InvalidFile("Invalid file extension");
        }

        storageService.store(file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "File was save successfully"));
    }

    @GetMapping("/list-file")
    public List<String> listFile() {

        if (storageService.loadAll().count() < 1)return new ArrayList<>();

        return storageService.loadAll()
                .map(path -> path.getFileName().toString()).toList();
    }

    @PostMapping("covert-to-html")
    public ResponseEntity<String> convertToHtml(@RequestParam("file") MultipartFile file) {

        if (!storageService.isValidExtension(file)) {
            throw new InvalidFile("Invalid file extension");
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_HTML).body(fileService.convertToHtml(file));
    }
}
