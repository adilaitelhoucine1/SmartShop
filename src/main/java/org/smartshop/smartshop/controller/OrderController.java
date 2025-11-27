package org.smartshop.smartshop.controller;


import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){this.orderService=orderService;}

    @GetMapping
    public ResponseEntity<List<OrderReadDTO>> getAllCOrders(){
        List<OrderReadDTO> orderList = orderService.getAllOrders();
        return ResponseEntity.ok(orderList);
    }
    @PostMapping
    public ResponseEntity<OrderReadDTO> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO){
        OrderReadDTO order=orderService.createCommande(orderCreateDTO);
        return ResponseEntity.ok(order);
    }
    @GetMapping("{id}/cancel")
    public ResponseEntity<OrderReadDTO> cancelOrder(@PathVariable("id") Long id){
       OrderReadDTO order=orderService.cancelOrder(id);
       return ResponseEntity.ok(order);
   }
    @GetMapping("{id}/reject")
    public ResponseEntity<OrderReadDTO> rejectOrder(@PathVariable("id") Long id){
        OrderReadDTO order=orderService.rejectOrder(id);
        return ResponseEntity.ok(order);
    }
}
