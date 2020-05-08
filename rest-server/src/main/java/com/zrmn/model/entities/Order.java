package com.zrmn.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order
{
    public enum Status
    {
        NEW, PROCESSING, DELIVERED
    }

    public enum Payment
    {
        CASH, CARD
    }

    public enum Delivery
    {
        COURIER, PICKUP
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private LocalDateTime created;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Enumerated(value = EnumType.STRING)
    private Payment payment;
    @Enumerated(value = EnumType.STRING)
    private Delivery delivery;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;
    private BigDecimal totalPrice;
}
