package org.smartshop.smartshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartshop.smartshop.DTO.payment.PaymentCreateDTO;
import org.smartshop.smartshop.DTO.payment.PaymentReadDTO;
import org.smartshop.smartshop.entity.Order;
import org.smartshop.smartshop.entity.Payment;
import org.smartshop.smartshop.enums.OrderStatus;
import org.smartshop.smartshop.enums.PaymentStatus;
import org.smartshop.smartshop.enums.PaymentType;
import org.smartshop.smartshop.exception.BusinessLogicException;
import org.smartshop.smartshop.exception.ResourceNotFoundException;
import org.smartshop.smartshop.mapper.PaymentMapper;
import org.smartshop.smartshop.repository.OrderRepository;
import org.smartshop.smartshop.repository.PaymentRepository;
import org.smartshop.smartshop.service.Impl.PaymentServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Service Tests")
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order testOrder;
    private Payment testPayment;
    private PaymentCreateDTO paymentCreateDTO;
    private PaymentReadDTO paymentReadDTO;

    @BeforeEach
    void setUp() {
        testOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.PENDING)
                .grandTotalTTC(new BigDecimal("5000.00"))
                .remainingAmount(new BigDecimal("5000.00"))
                .payments(new ArrayList<>())
                .build();

        testPayment = Payment.builder()
                .id(1L)
                .amount(new BigDecimal("2000.00"))
                .paymentType(PaymentType.ESPECES)
                .status(PaymentStatus.EN_ATTENTE)
                .reference("REF123")
                .order(testOrder)
                .build();

        paymentCreateDTO = new PaymentCreateDTO();
        paymentCreateDTO.setAmount(new BigDecimal("2000.00"));
        paymentCreateDTO.setPaymentType(PaymentType.ESPECES);
        paymentCreateDTO.setReference("REF123");
        paymentCreateDTO.setOrderId(1L);

        paymentReadDTO = new PaymentReadDTO();
        paymentReadDTO.setId(1L);
        paymentReadDTO.setAmount(new BigDecimal("2000.00"));
        paymentReadDTO.setPaymentType(PaymentType.ESPECES);
        paymentReadDTO.setStatus(PaymentStatus.ENCAISSE);
        paymentReadDTO.setReference("REF123");
        paymentReadDTO.setOrderId(1L);
    }

    @Test
    @DisplayName("Create payment with ESPECES - Should succeed and auto-validate")
    void createPayment_WithEspeces_ShouldSucceed() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(paymentMapper.toReadDTO(any(Payment.class))).thenReturn(paymentReadDTO);

        PaymentReadDTO result = paymentService.createPayment(paymentCreateDTO);

        assertNotNull(result);
        assertEquals("REF123", result.getReference());
        assertEquals(PaymentStatus.ENCAISSE, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Create payment with CHEQUE - Should include due date and bank name")
    void createPayment_WithCheque_ShouldSucceed() {
        paymentCreateDTO.setPaymentType(PaymentType.CHEQUE);
        paymentCreateDTO.setDueDate(LocalDate.now().plusDays(30));
        paymentCreateDTO.setBankName("Bank ABC");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(paymentMapper.toReadDTO(any(Payment.class))).thenReturn(paymentReadDTO);

        PaymentReadDTO result = paymentService.createPayment(paymentCreateDTO);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Create payment for non-existent order - Should throw ResourceNotFoundException")
    void createPayment_OrderNotFound_ShouldThrowException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            paymentService.createPayment(paymentCreateDTO)
        );
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Create ESPECES payment exceeding limit - Should throw BusinessLogicException")
    void createPayment_EspecesExceedingLimit_ShouldThrowException() {
        paymentCreateDTO.setAmount(new BigDecimal("25000.00"));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () ->
            paymentService.createPayment(paymentCreateDTO)
        );
        assertEquals("Reached Limit 20 000 Dh", exception.getMessage());
    }

    @Test
    @DisplayName("Validate payment - Should update status and reduce remaining amount")
    void validatePayment_ShouldSucceed() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(paymentMapper.toReadDTO(any(Payment.class))).thenReturn(paymentReadDTO);

        PaymentReadDTO result = paymentService.validatePayment(1L);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Reject payment - Should update status to REJETE")
    void rejectPayment_ShouldSucceed() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        
        paymentReadDTO.setStatus(PaymentStatus.REJETE);
        when(paymentMapper.toReadDTO(any(Payment.class))).thenReturn(paymentReadDTO);

        PaymentReadDTO result = paymentService.rejectPayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.REJETE, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}