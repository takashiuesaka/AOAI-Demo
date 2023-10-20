package com.microsoft.aoai.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microsoft.aoai.demo.repository.AnalyzeImageRepository;

@Service
public class AnalyzeImageService {
    private AnalyzeImageRepository analyzeImageRepository;

    public AnalyzeImageService(AnalyzeImageRepository analyzeImageRepository) {
        this.analyzeImageRepository = analyzeImageRepository;
    }

    public List<String> analyze(String filePath) {
        return analyzeImageRepository.analyze(filePath);
    }
}
