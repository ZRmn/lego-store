package com.zrmn.model.services;

import com.zrmn.model.entities.Customer;
import com.zrmn.model.entities.Order;
import com.zrmn.model.entities.OrderItem;
import com.zrmn.model.exceptions.BadRequestException;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.forms.OrderForm;
import com.zrmn.model.repositories.CartItemsRepository;
import com.zrmn.model.repositories.CustomersRepository;
import com.zrmn.model.repositories.OrdersRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService
{
    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private StockService stockService;

    @Override
    public List<Order> getAll()
    {
        return ordersRepository.findAll();
    }

    @Override
    public List<Order> getAll(Customer customer)
    {
        return customer.getOrders();
    }

    @Override
    public Order get(Long id) throws NotFoundException
    {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such order"));
    }

    @Override
    public Order get(Customer customer, Long id) throws NotFoundException
    {
        return customer.getOrders().stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Customer has no such order"));
    }

    @Override
    public void save(Customer customer, Order order)
    {
        ordersRepository.save(order);
        customer.getOrders().add(order);
        customersRepository.save(customer);
    }

    @Override
    public void delete(Long id) throws NotFoundException
    {
        ordersRepository.delete(get(id));
    }

    @Override
    public void deleteAll()
    {
        ordersRepository.deleteAll();
    }

    @Override
    public void deleteAll(Customer customer)
    {
        customer.getOrders().forEach(order -> ordersRepository.delete(order));
        customer.getOrders().clear();
    }

    @Override
    public void update(Order order) throws NotFoundException
    {
        get(order.getId());
        ordersRepository.save(order);
    }

    @Override
    public Order placeOrder(Customer customer, OrderForm orderForm) throws NotFoundException, BadRequestException
    {
        if (customer.getCartItems().isEmpty())
        {
            throw new BadRequestException("Cart is empty");
        }

        List<OrderItem> orderItems = customer.getCartItems().stream()
                .map(cartItem -> {
                    if(stockService.getAvailability(cartItem.getProduct()) < cartItem.getQuantity())
                    {
                        throw new BadRequestException("Product out of stock");
                    }

                    return new OrderItem(null, cartItem.getProduct(), cartItem.getQuantity());
                })
                .collect(Collectors.toList());

        cartService.deleteAll(customer);
        customer = customersRepository.findById(customer.getId()).get();

        BigDecimal totalPrice = orderItems.stream()
                .map(orderItem -> orderItem.getProduct().getPrice())
                .reduce(new BigDecimal(0), BigDecimal::add);

        Order order = Order.builder()
                .address(orderForm.getAddress())
                .created(LocalDateTime.now())
                .status(Order.Status.NEW)
                .payment(orderForm.getPayment())
                .delivery(orderForm.getDelivery())
                .orderItems(orderItems)
                .totalPrice(totalPrice).build();

        save(customer, order);
        return order;
    }

    @Override
    public void setStatus(Long id, Order.Status status) throws NotFoundException
    {
        Order order = get(id);
        order.setStatus(status);
        ordersRepository.save(order);
    }
}
