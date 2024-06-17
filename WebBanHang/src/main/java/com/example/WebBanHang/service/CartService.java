package com.example.WebBanHang.service;

import com.example.WebBanHang.model.CartItem;
import com.example.WebBanHang.model.Product;
import com.example.WebBanHang.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        int totalQuantityInCart = quantity;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                totalQuantityInCart += item.getQuantity();
            }
        }

        if (totalQuantityInCart > product.getQuantity()) {
            throw new IllegalArgumentException("Vượt quá số lượng có sẵn trong kho.");
        }

        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        cartItems.add(new CartItem(product, quantity));
    }

    public void increaseQuantity(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                if (item.getQuantity() + 1 > product.getQuantity()) {
                    throw new IllegalArgumentException("Vượt quá số lượng có sẵn trong kho.");
                }
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
    }

    public void decreaseQuantity(Long productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    cartItems.remove(item);
                }
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart() {
        cartItems.clear();
    }

}
