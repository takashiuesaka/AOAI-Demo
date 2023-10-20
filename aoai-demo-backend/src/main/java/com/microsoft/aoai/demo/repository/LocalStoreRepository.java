package com.microsoft.aoai.demo.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.aoai.demo.model.Settings;

@Repository
public class LocalStoreRepository {

    Logger logger = Logger.getLogger(LocalStoreRepository.class.getName());
    private Settings settings;
    private Path path;

    public LocalStoreRepository(Settings settings) {
        this.settings = settings;
        this.path = Paths.get(this.settings.getTmpDir());
    }

    public void init() throws Exception {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new Exception("Create tmp directory has failed.");
        }
    }

    public String store(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Upload file is empty.");
        }
        Path destinationFile = this.path.resolve(
                Paths.get(file.getName() + "." +
                        FilenameUtils.getExtension(file.getOriginalFilename())))
                .normalize()
                .toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new Exception("Failed to store file.", e);
        }
        return destinationFile.toString();
    }
}
