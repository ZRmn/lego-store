package com.zrmn.model.services;

import com.zrmn.model.entities.Customer;
import com.zrmn.model.entities.Order;
import com.zrmn.model.exceptions.BadRequestException;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.forms.OrderForm;

import java.util.List;

public interface OrdersService
{
    List<Order> getAll();

    List<Order> getAll(Customer customer);

    Order get(Long id) throws NotFoundException;

    Order get(Customer customer, Long id) throws NotFoundException;

    void save(Customer customer, Order order);

    void delete(Long id) throws NotFoundException;

    void deleteAll();

    void deleteAll(Customer customer);

    void update(Order order) throws NotFoundException;

    Order placeOrder(Customer customer, OrderForm orderForm) throws NotFoundException, BadRequestException;

    void setStatus(Long id, Order.Status status) throws NotFoundException;
}
