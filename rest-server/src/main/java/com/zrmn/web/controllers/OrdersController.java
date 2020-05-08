package com.zrmn.web.controllers;

import com.zrmn.model.entities.Customer;
import com.zrmn.model.entities.Order;
import com.zrmn.model.forms.OrderForm;
import com.zrmn.model.services.OrdersService;
import com.zrmn.model.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController
{
    @Autowired
    private UsersService usersService;

    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public ResponseEntity getOrders(Authentication authentication)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        List<Order> orders = ordersService.getAll(customer);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrder(Authentication authentication, @PathVariable Long id)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        Order order = ordersService.get(customer, id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity placeOrder(Authentication authentication, @RequestBody OrderForm orderForm)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        Order order = ordersService.placeOrder(customer, orderForm);
        return ResponseEntity.ok(order);
    }
}
