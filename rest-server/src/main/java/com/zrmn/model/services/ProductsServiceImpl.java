package com.zrmn.model.services;

import com.zrmn.model.entities.Product;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.repositories.ProductsRepository;
import com.zrmn.utils.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService
{
    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public synchronized List<Product> getAll()
    {
        return productsRepository.findAll();
    }

    @Override
    public synchronized Product get(Long id) throws NotFoundException
    {
        return productsRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
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
    public synchronized SearchEngine.Searcher<Product> searcher()
    {
        return SearchEngine.searcher(getAll());
    }
}
