package com.zrmn.model.services;

import com.zrmn.model.entities.Product;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.utils.SearchEngine;

import java.util.List;

public interface ProductsService
{
    List<Product> getAll();

    Product get(Long id) throws NotFoundException;

    void save(Product product);

    void delete(Long id) throws NotFoundException;

    void deleteAll();

    void update(Product product) throws NotFoundException;

    SearchEngine.Searcher<Product> searcher();
}
