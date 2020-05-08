package com.zrmn.model.services;

import com.zrmn.model.entities.CartItem;
import com.zrmn.model.entities.Customer;
import com.zrmn.model.entities.Product;
import com.zrmn.model.exceptions.BadRequestException;
import com.zrmn.model.exceptions.NotFoundException;

import java.util.List;

public interface CartService
{
    List<CartItem> getAll();

    List<CartItem> getAll(Customer customer);

    CartItem get(Long id) throws NotFoundException;

    CartItem get(Customer customer, Long id) throws NotFoundException;

    void save(Customer customer, CartItem cartItem);

    void delete(Long id) throws NotFoundException;

    void delete(Customer customer, Long id) throws NotFoundException;

    void deleteAll();

    void deleteAll(Customer customer);

    void update(CartItem cartItem) throws NotFoundException;

    void update(Customer customer, CartItem cartItem) throws NotFoundException;

    CartItem addToCart(Customer customer, Product product, Integer quantity) throws NotFoundException, BadRequestException;
}
