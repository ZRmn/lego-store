package com.zrmn.model.forms;

import com.zrmn.model.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderForm
{
    private String address;
    private Order.Payment payment;
    private Order.Delivery delivery;
}
