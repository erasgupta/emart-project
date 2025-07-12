package com.portal.emart.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.portal.emart.api.dao.OrderRepository;
import com.portal.emart.api.dao.UserRepository;
import com.portal.emart.api.entity.Customers;
import com.portal.emart.api.entity.Order;
import com.portal.emart.api.model.OrderRequest;

@Service
public class OrderService {

    private final UserRepository customerRepository;
    private final OrderRepository orderRepository;
    private final MailService mailService;
    
    public OrderService(UserRepository customerRepository, OrderRepository orderRepository,MailService mailService) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }

    public void placeOrder(OrderRequest request) {
    	
    	// Validate delivery date
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(today, request.getDeliveryDate());
        if (daysBetween <= 2) {
            throw new IllegalArgumentException("Delivery date must be at least 2 days from today.");
        }
    	
        // Check if customer exists
        Customers customer = customerRepository.findByEmailOrPhone(request.getEmail(), request.getPhone());
		if (customer == null) {
            // Create new customer
            Customers newCustomer = new Customers();
            newCustomer.setFirstName(request.getFirstName());
            newCustomer.setLastName(request.getLastName());
            newCustomer.setEmail(request.getEmail());
            newCustomer.setPhone(request.getPhone());
            customer = customerRepository.save(newCustomer);
        }

        // Create new order
        Order order = new Order();
        order.setFirstName(request.getFirstName());
        order.setLastName(request.getLastName());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setCakeType(request.getCakeType());
        order.setFlavor(request.getFlavor());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setMessage(request.getMessage());
        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);
        
        mailService.sendWelcomeEmail(order.getEmail(), order.getFirstName(),order);
    }
}
