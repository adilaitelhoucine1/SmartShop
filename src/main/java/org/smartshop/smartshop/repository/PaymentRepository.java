package org.smartshop.smartshop.repository;

import org.smartshop.smartshop.entity.Order;
import org.smartshop.smartshop.entity.Payment;
import org.smartshop.smartshop.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> getAllByOrderAndStatus(Order order, PaymentStatus status);
}