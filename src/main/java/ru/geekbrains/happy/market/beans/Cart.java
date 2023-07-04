package ru.geekbrains.happy.market.beans;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.happy.market.exceptions_handling.ResourceNotFoundException;
import ru.geekbrains.happy.market.model.OrderItem;
import ru.geekbrains.happy.market.model.Product;
import ru.geekbrains.happy.market.services.ProductService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class Cart {
    private final ProductService productService;
    private List<OrderItem> items;
    private int totalPrice;
    //private final OrderItem orderItem;

    @PostConstruct
    public void init() {
        this.items = new ArrayList<>();
    }

    public void addToCart(Long id) {
        for (OrderItem o : items) {
            if (o.getProduct().getId().equals(id)) {
                o.incrementQuantity();
                recalculate();
                return;
            }
        }
        Product p = productService.findProductById(id).orElseThrow(() -> new ResourceNotFoundException("Unable to find product with id: " + id + " (add to cart)"));
        OrderItem orderItem = new OrderItem(p);
        items.add(orderItem);
        recalculate();
    }

    public void clear() {
        items.clear();
        recalculate();
    }

    public void plus(int pricePerProduct) {
        for(OrderItem item : items) {
            if(item.getPricePerProduct() == pricePerProduct){
                item.setQuantity(item.getQuantity() + 1);
                item.setPrice(item.getPricePerProduct() * item.getQuantity());
                recalculate();
            }
        }
    }

    public void minus(int pricePerProduct) {
        int i = 0;
        for(OrderItem item : items) {
            i++;
            if(item.getPricePerProduct() == pricePerProduct){
                item.setQuantity(item.getQuantity() - 1);
                item.setPrice(item.getPricePerProduct() * item.getQuantity());
                recalculate();
                break;
            }
        }
        if(items.get(i-1).getQuantity() <= 0){
            items.remove(i-1);
        }
    }

    public void deleteItem(int pricePerProduct) {
        int i = 0;
        for(OrderItem item : items) {
            if(item.getPricePerProduct() == pricePerProduct){
                items.remove(i);
                break;
            }
            i++;
        }
    }

    public void recalculate() {
        totalPrice = 0;
        for (OrderItem o : items) {
            totalPrice += o.getPrice();
        }
    }
}
