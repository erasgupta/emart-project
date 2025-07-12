package com.portal.emart.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal.emart.api.model.OrderRequest;
import com.portal.emart.api.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("emart/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        orderService.placeOrder(request);
        return ResponseEntity.ok("Order request submit successfully");
    }
}
