package org.smartshop.smartshop.repository;

import org.smartshop.smartshop.entity.OrderItem;
import org.smartshop.smartshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByProduct(Product product);
}