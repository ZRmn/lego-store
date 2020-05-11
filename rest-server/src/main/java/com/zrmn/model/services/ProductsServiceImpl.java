package com.zrmn.model.services;

import com.zrmn.model.entities.Product;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.forms.ProductForm;
import com.zrmn.model.repositories.ProductsRepository;
import com.zrmn.utils.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsServiceImpl implements ProductsService
{
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public synchronized List<Product> getAll()
    {
        return productsRepository.findAll();
    }

    @Override
    public synchronized Product get(Long id) throws NotFoundException
    {
        return productsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public synchronized void save(Product product)
    {
        productsRepository.save(product);
    }

    @Override
    public void delete(Long id) throws NotFoundException
    {
        productsRepository.delete(get(id));
    }

    @Override
    public void deleteAll()
    {
        productsRepository.deleteAll();
    }

    @Override
    public synchronized void update(Product product) throws NotFoundException
    {
        get(product.getId());
        productsRepository.save(product);
    }

    @Override
    public void saveProductAndImages(ProductForm productForm)
    {
        List<MultipartFile> images = productForm.getImages();
        List<String> imageUrls = images.stream()
                .filter(multipartFile -> !multipartFile.isEmpty())
                .map(multipartFile -> {
                    String path = "/" + productForm.getCategory() + "/"
                            + productForm.getArticle() + "/"
                            + multipartFile.getOriginalFilename();

                    fileStorageService.store(path, multipartFile);

                    return path;
                })
                .collect(Collectors.toList());

        Product product = Product.builder()
                .title(productForm.getTitle())
                .article(productForm.getArticle())
                .category(productForm.getCategory())
                .description(productForm.getDescription())
                .pieces(productForm.getPieces())
                .price(productForm.getPrice())
                .releaseDate(productForm.getReleaseDate())
                .imageUrls(imageUrls)
                .build();

        save(product);
    }

    private void deleteImages(Product product)
    {
        product.getImageUrls().forEach(s -> {
            fileStorageService.delete(s);
        });

        if(!product.getImageUrls().isEmpty())
        {
            Path directoryPath = Paths.get(product.getImageUrls().get(0)).getParent();
            fileStorageService.deleteEmptyDirectories(directoryPath.toString());
        }
    }

    @Override
    public void deleteProductAndImages(Long id) throws NotFoundException
    {
        Product product = get(id);
        deleteImages(product);
        delete(product.getId());
    }

    @Override
    public void deleteAllProductsAndImages()
    {
        productsRepository.findAll().forEach(this::deleteImages);
        deleteAll();
    }

    @Override
    public void updateProductAndImages(ProductForm productForm) throws NotFoundException
    {
        deleteProductAndImages(productForm.getId());
        saveProductAndImages(productForm);
    }

    @Override
    public synchronized SearchEngine.Searcher<Product> searcher()
    {
        return SearchEngine.searcher(getAll());
    }
}
