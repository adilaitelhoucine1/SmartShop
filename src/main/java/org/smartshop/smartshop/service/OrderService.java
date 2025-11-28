package org.smartshop.smartshop.service;

import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;

import java.util.List;

public interface OrderService {
    List<OrderReadDTO> getAllOrders();

    OrderReadDTO createCommande(@Valid OrderCreateDTO orderCreateDTO);

    OrderReadDTO cancelOrder(Long id);

    OrderReadDTO rejectOrder(Long id);

    List<OrderReadDTO> getOrderHistoryByClient(Long clientId);
    OrderReadDTO validateOrder(Long orderId);
}
