package com.eb2.markdownnotetakingapp.service;

import com.eb2.markdownnotetakingapp.config.StorageProperties;
import com.eb2.markdownnotetakingapp.exception.EmptyFileException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StorageService {


    public static final String[] EXTENSION = {"md", "txt"};

    private final Path rootLocation;

    public StorageService(StorageProperties properties) {

        if (properties.getPath().trim().isEmpty()) {
            throw new IllegalArgumentException("Path is empty");
        }

        this.rootLocation = Paths.get(properties.getPath());

    }

    public void store(MultipartFile file) {

        try {
            if (file.isEmpty()) {
                throw new EmptyFileException("File is empty");
            }

            Path destinationPath = this.rootLocation
                    .resolve(UUID.randomUUID().toString())
                    .normalize().toAbsolutePath();

            if (!destinationPath.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new IllegalArgumentException("Destination Path is not valid");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidExtension(MultipartFile file) {
        return FilenameUtils.isExtension(file.getOriginalFilename(), EXTENSION);
    }
}
