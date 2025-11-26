package org.smartshop.smartshop.repository;

import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.entity.Client;
import org.smartshop.smartshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByClient(Client client);
}