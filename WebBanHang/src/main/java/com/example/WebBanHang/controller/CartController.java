package com.example.WebBanHang.controller;

import com.example.WebBanHang.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        return "cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(productId, quantity);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @PostMapping("/increase/{productId}")
    public String increaseQuantity(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        try {
            cartService.increaseQuantity(productId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/decrease/{productId}")
    public String decreaseQuantity(@PathVariable Long productId) {
        cartService.decreaseQuantity(productId);
        return "redirect:/cart";
    }
}
