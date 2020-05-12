package com.zrmn.model.services;

import com.zrmn.model.entities.CartItem;
import com.zrmn.model.entities.Customer;
import com.zrmn.model.entities.Product;
import com.zrmn.model.exceptions.BadRequestException;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.repositories.CartItemsRepository;
import com.zrmn.model.repositories.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService
{
    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private StockService stockService;

    @Override
    public List<CartItem> getAll()
    {
        return cartItemsRepository.findAll();
    }

    @Override
    public List<CartItem> getAll(Customer customer)
    {
        return customer.getCartItems();
    }

    @Override
    public CartItem get(Long id) throws NotFoundException
    {
        return cartItemsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such cart item"));
    }

    @Override
    public CartItem get(Customer customer, Long id) throws NotFoundException
    {
        return customer.getCartItems().stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Customer has no such cart item"));
    }

    @Override
    public void save(Customer customer, CartItem cartItem)
    {
        cartItemsRepository.save(cartItem);
        customer.getCartItems().add(cartItem);
        customersRepository.save(customer);
    }

    @Override
    public void delete(Long id) throws NotFoundException
    {
        cartItemsRepository.delete(get(id));
    }

    @Override
    public void delete(Customer customer, Long id) throws NotFoundException
    {
        cartItemsRepository.delete(get(customer, id));
    }

    @Override
    public void deleteAll()
    {
        cartItemsRepository.deleteAll();
    }

    @Override
    public void deleteAll(Customer customer)
    {
        customer.getCartItems().forEach(cartItem -> cartItemsRepository.delete(cartItem));
        customer.getCartItems().clear();
    }

    @Override
    public void update(CartItem cartItem) throws NotFoundException
    {
        get(cartItem.getId());
        cartItemsRepository.save(cartItem);
    }

    @Override
    public void update(Customer customer, CartItem cartItem) throws NotFoundException
    {
        get(customer, cartItem.getId());
        cartItemsRepository.save(cartItem);
    }

    @Override
    public CartItem addToCart(Customer customer, Product product, Integer quantity) throws NotFoundException, BadRequestException
    {
        Optional<CartItem> cartItemCandidate = customer.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst();

        CartItem cartItem;
        int productAvailability = stockService.getAvailability(product);

        if(cartItemCandidate.isPresent())
        {
            cartItem = cartItemCandidate.get();

            if(productAvailability < cartItem.getQuantity() + quantity)
            {
                throw new BadRequestException("Product out of stock");
            }

            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        else
        {
            if(productAvailability < quantity)
            {
                throw new BadRequestException("Product out of stock");
            }

            cartItem = new CartItem(null, product, quantity);
            customer.getCartItems().add(cartItem);
        }

        save(customer, cartItem);

        return cartItem;
    }
}
