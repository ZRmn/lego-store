package com.zrmn.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User
{
    public enum Role
    {
        ADMIN, CUSTOMER
    }

    public enum State
    {
        ACTIVE, BANNED, DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String login;
    protected String password;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "user_id")
    protected List<Authority> authorities;
    @Enumerated(value = EnumType.STRING)
    protected State state;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "user_id")
    protected List<Token> tokens;
}
