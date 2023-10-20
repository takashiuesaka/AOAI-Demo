package com.microsoft.aoai.demo.service;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.aoai.demo.repository.LocalStoreRepository;

@Service
public class LocalStoreService {

    Logger logger = Logger.getLogger(LocalStoreService.class.getName());
    private LocalStoreRepository localStoreRepository;

    public LocalStoreService(LocalStoreRepository localStoreRepository) {
        this.localStoreRepository = localStoreRepository;
    }

    public void init() throws Exception {
        localStoreRepository.init();
    }

    public String store(MultipartFile file) throws Exception {
        return localStoreRepository.store(file);
    }
}
