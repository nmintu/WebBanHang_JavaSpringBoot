package com.example.WebBanHang.service;

import com.example.WebBanHang.model.CartItem;
import com.example.WebBanHang.model.Order;
import com.example.WebBanHang.model.OrderDetail;
import com.example.WebBanHang.model.Product;
import com.example.WebBanHang.repository.OrderDetailRepository;
import com.example.WebBanHang.repository.OrderRepository;
import com.example.WebBanHang.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductRepository productRepository;
    @Transactional
    public Order createOrder(Order order, List<CartItem> cartItems) {
        orderRepository.save(order);

        for(CartItem item : cartItems){
            Product product = item.getProduct();
            int newQuantity = product.getQuantity() - item.getQuantity();
            if(newQuantity <0){
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            product.setQuantity(newQuantity);
            productRepository.save(product);

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        cartService.clearCart();
        return order;
    }
}


