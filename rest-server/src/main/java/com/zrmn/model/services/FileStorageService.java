package com.zrmn.model.services;

import com.zrmn.model.exceptions.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileStorageService
{
    void store(String filePath, MultipartFile file);

    File load(String filePath) throws NotFoundException;

    Resource loadAsResource(String filePath) throws NotFoundException;

    void delete(String filePath) throws NotFoundException;

    void deleteEmptyDirectories(String directoryPath);
}
