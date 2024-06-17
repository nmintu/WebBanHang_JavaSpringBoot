package com.example.WebBanHang.controller;

import com.example.WebBanHang.model.CartItem;
import com.example.WebBanHang.model.Order;
import com.example.WebBanHang.model.Product;
import com.example.WebBanHang.service.CartService;
import com.example.WebBanHang.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @GetMapping("/checkout")
    public String checkout(){
        return "/cart/checkout";
    }
    @GetMapping("/comfirm")
    public String orderconfirmation(){
        return "/cart/order-confirmation";
    }
    @PostMapping("/submit")
    public String submitOrder(@Valid @ModelAttribute Order order, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/cart/checkout";
        }

        List<CartItem> cartItems = cartService.getCartItems();
        if(cartItems.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn trống.");
            return "redirect:/cart";
        }

        try {
            orderService.createOrder(order, cartItems);
            return "redirect:/order/comfirm";
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/checkout";
        }
    }


}
