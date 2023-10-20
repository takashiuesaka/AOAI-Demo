package com.microsoft.aoai.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.microsoft.aoai.demo.model.Settings;

@Service
public class DeleteUploadedFileService {

    Logger logger = Logger.getLogger(DeleteUploadedFileService.class.getName());
    private Settings settings;
    private Path path;

    public DeleteUploadedFileService(Settings settings) {
        this.settings = settings;
        this.path = Paths.get(settings.getTmpDir());
    }

    public void deleteTmpFile() throws IOException {
        Files.walk(this.path, 1).forEach(path -> {
            try {
                Files.deleteIfExists(path);
                logger.info("delete file: " + path.toString());
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }
        });
    }
}
