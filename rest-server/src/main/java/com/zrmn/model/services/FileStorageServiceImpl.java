package com.zrmn.model.services;

import com.zrmn.model.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService
{
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void store(String filePath, MultipartFile file)
    {
        Path path = Paths.get(uploadPath + filePath);

        try (InputStream inputStream = file.getInputStream())
        {
            if(!Files.exists(path.getParent()))
            {
                Files.createDirectories(path.getParent());
            }

            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public File load(String filePath) throws NotFoundException
    {
        File file = Paths.get(uploadPath + filePath).toFile();

        if(!file.exists())
        {
            throw new NotFoundException("File not found");
        }

        return file;
    }

    @Override
    public Resource loadAsResource(String filePath) throws NotFoundException
    {
        try
        {
            File file = load(filePath);
            return new UrlResource(file.toURI());
        }
        catch (MalformedURLException e)
        {
            throw new NotFoundException("File not found");
        }
    }

    @Override
    public void delete(String filePath) throws NotFoundException
    {
        try
        {
            File file = load(filePath);
            file.delete();
        }
        catch (NotFoundException e)
        {
            //
        }
    }

    private boolean isEmptyDirectory(File file)
    {
        return file.isDirectory() && file.list().length == 0;
    }

    @Override
    public void deleteEmptyDirectories(String directoryPath)
    {
        File file = new File(uploadPath + directoryPath);

        while (!file.getName().endsWith(uploadPath))
        {
            if(isEmptyDirectory(file))
            {
                file.delete();
            }
            else
            {
                break;
            }

            file = file.getParentFile();
        }
    }
}
