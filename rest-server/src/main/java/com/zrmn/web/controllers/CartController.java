package com.zrmn.web.controllers;

import com.zrmn.model.entities.CartItem;
import com.zrmn.model.entities.Customer;
import com.zrmn.model.entities.Product;
import com.zrmn.model.forms.CartItemForm;
import com.zrmn.model.services.CartService;
import com.zrmn.model.services.ProductsService;
import com.zrmn.model.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController
{
    @Autowired
    private UsersService usersService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductsService productsService;

    @GetMapping
    public ResponseEntity getCartItems(Authentication authentication)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        List<CartItem> cartItems = cartService.getAll(customer);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCartItem(Authentication authentication, @PathVariable Long id)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        CartItem cartItem = cartService.get(customer, id);
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping
    public ResponseEntity addCartItem(Authentication authentication, @RequestBody CartItemForm cartItemForm)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        Product product = productsService.get(cartItemForm.getProductId());
        int quantity = cartItemForm.getQuantity();
        CartItem cartItem = cartService.addToCart(customer, product, quantity);

        return ResponseEntity.ok(cartItem);
    }

    @PutMapping
    public ResponseEntity updateCartItem(Authentication authentication, @RequestBody CartItem cartItem)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        cartService.update(customer, cartItem);
        return ResponseEntity.ok("Cart item updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCartItem(Authentication authentication, @PathVariable Long id)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        cartService.delete(customer, id);
        return ResponseEntity.ok("Cart item deleted");
    }

    @DeleteMapping
    public ResponseEntity deleteCartItems(Authentication authentication)
    {
        Customer customer = (Customer) usersService.getFromAuthentication(authentication);
        cartService.deleteAll(customer);
        return ResponseEntity.ok("Cart items deleted");
    }
}
