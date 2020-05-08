package com.zrmn.model.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "customers")
public class Customer extends User
{
    private String fullName;
    private String phoneNumber;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "customer_id")
    private List<CartItem> cartItems;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "customer_id")
    private List<Order> orders;
}
