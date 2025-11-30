package org.smartshop.smartshop.controller;


import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.service.OrderService;
import org.smartshop.smartshop.utils.SecurityAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){this.orderService=orderService;}

    @GetMapping
    public ResponseEntity<List<OrderReadDTO>> getAllCOrders(HttpSession session){
        SecurityAuth.requireAdmin(session);
        List<OrderReadDTO> orderList = orderService.getAllOrders();
        return ResponseEntity.ok(orderList);
    }
    @PostMapping
    public ResponseEntity<OrderReadDTO> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO,HttpSession session){
        SecurityAuth.requireAdmin(session);
        OrderReadDTO order=orderService.createCommande(orderCreateDTO);
        return ResponseEntity.ok(order);
    }
    @GetMapping("{id}/cancel")
    public ResponseEntity<OrderReadDTO> cancelOrder(@PathVariable("id") Long id,HttpSession session){
        SecurityAuth.requireAdmin(session);
       OrderReadDTO order=orderService.cancelOrder(id);
       return ResponseEntity.ok(order);
   }
    @GetMapping("{id}/reject")
    public ResponseEntity<OrderReadDTO> rejectOrder(@PathVariable("id") Long id,HttpSession session){
        SecurityAuth.requireAdmin(session);
        OrderReadDTO order=orderService.rejectOrder(id);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/{clientId}/orderHistory")
    public ResponseEntity<List<OrderReadDTO>> getOrderHistoryByClient(@PathVariable("clientId") Long clientId,
                            HttpSession session) {
        SecurityAuth.requireClient(session);
        List<OrderReadDTO> orderHistory=orderService.getOrderHistoryByClient(clientId);
        return ResponseEntity.ok(orderHistory);
    }

    @GetMapping("{orderId}/validate")
    public ResponseEntity<OrderReadDTO> validateOrder(@PathVariable("orderId") Long orderId,HttpSession session){
        SecurityAuth.requireAdmin(session);
        OrderReadDTO order=orderService.validateOrder(orderId);
        return ResponseEntity.ok(order);
    }


}
