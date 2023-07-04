package ru.geekbrains.happy.market.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.happy.market.beans.Cart;
import ru.geekbrains.happy.market.dto.CartDto;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final Cart cart;

    @GetMapping
    public CartDto getCart() {
        return new CartDto(cart);
    }

    @GetMapping("/add/{id}")
    public void addToCart(@PathVariable Long id) {
        cart.addToCart(id);
    }

    @GetMapping("/clear")
    public void clearCart() {
        cart.clear();
    }


    @GetMapping("/items/{pricePerProduct}")
    public void plusTo(@PathVariable int pricePerProduct) {
        if(pricePerProduct>0) {
            pricePerProduct=pricePerProduct-10000;
            cart.plus(pricePerProduct);
        } else {
            pricePerProduct+=10000;
            if(pricePerProduct>0){
                cart.minus(pricePerProduct);
            } else {
                pricePerProduct+=10000;
                cart.deleteItem(pricePerProduct);
            }
        }
    }



}
