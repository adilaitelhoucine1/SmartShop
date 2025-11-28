package org.smartshop.smartshop.service.Impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.DTO.payment.PaymentCreateDTO;
import org.smartshop.smartshop.DTO.payment.PaymentReadDTO;
import org.smartshop.smartshop.entity.Client;
import org.smartshop.smartshop.entity.Order;
import org.smartshop.smartshop.entity.Payment;
import org.smartshop.smartshop.enums.PaymentStatus;
import org.smartshop.smartshop.enums.PaymentType;
import org.smartshop.smartshop.exception.BusinessLogicException;
import org.smartshop.smartshop.exception.LimiteEspeceException;
import org.smartshop.smartshop.exception.OrderAlreadyPayed;
import org.smartshop.smartshop.exception.ResourceNotFoundException;
import org.smartshop.smartshop.mapper.PaymentMapper;
import org.smartshop.smartshop.repository.ClientRepository;
import org.smartshop.smartshop.repository.OrderItemRepository;
import org.smartshop.smartshop.repository.OrderRepository;
import org.smartshop.smartshop.repository.PaymentRepository;
import org.smartshop.smartshop.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private ClientRepository clientRepository;
    private final PaymentMapper paymentMapper;

    public List<PaymentReadDTO> getOrderPaymentsWithStatusPending(Order order) {
        List<Payment> payments = paymentRepository.getAllByOrderAndStatus(order,PaymentStatus.EN_ATTENTE);
        return payments.stream().map(paymentMapper::toReadDTO).toList();
    }

   public PaymentReadDTO createPayment(@Valid PaymentCreateDTO paymentDTO){
       Order order= orderRepository.findById(paymentDTO.getOrderId()).orElseThrow(
               ()-> new ResourceNotFoundException("Order Not Found")
       );
       if (order.getRemainingAmount().equals(BigDecimal.ZERO)){
           throw  new BusinessLogicException("Order Already Payed");
       }

       if (paymentDTO.getPaymentType()==PaymentType.ESPECES
               && paymentDTO.getAmount().compareTo(new BigDecimal("20000"))>0){
           throw new BusinessLogicException("Reached Limit 20 000 Dh");

       }
       Payment payment=Payment.builder()
               .amount(paymentDTO.getAmount())
               .paymentType(paymentDTO.getPaymentType())
               .status(PaymentStatus.EN_ATTENTE)
               .reference(paymentDTO.getReference())
               .order(order)
               .build();
       paymentRepository.save(payment);

       if (paymentDTO.getPaymentType()==PaymentType.ESPECES){
          // payment.setBankName(paymentDTO.getBankName());
           payment.setStatus(PaymentStatus.ENCAISSE);
           validatePayment(payment.getId());
       }
       if (paymentDTO.getPaymentType()==PaymentType.VIREMENT){
           payment.setBankName(paymentDTO.getBankName());
       }
       if (paymentDTO.getPaymentType()==PaymentType.CHEQUE){
           payment.setBankName(paymentDTO.getBankName());
           payment.setDueDate(paymentDTO.getDueDate());
       }

       paymentRepository.save(payment);
       return paymentMapper.toReadDTO(payment);
    }


    public PaymentReadDTO validatePayment(Long paymentId){

        Payment paiment= paymentRepository.findById(paymentId).orElseThrow(
                ()-> new ResourceNotFoundException("Order Not Found")
        );

        Order ordre= orderRepository.findById(paiment.getOrder().getId()).orElseThrow(
                ()-> new ResourceNotFoundException("Order Not Found")
        );

        if (getOrderPaymentsWithStatusPending(ordre).isEmpty()){
            throw new BusinessLogicException("No Payment to validate All Are validated");
        }

        if (ordre.getRemainingAmount().compareTo(paiment.getAmount())<0){
                throw new BusinessLogicException("the Amount is more than  the paiment Amount");
        }

        ordre.setRemainingAmount(ordre.getRemainingAmount().subtract(paiment.getAmount()));

        paiment.setStatus(PaymentStatus.ENCAISSE);

        orderRepository.save(ordre);
        paymentRepository.save(paiment);
        return paymentMapper.toReadDTO(paiment);
    }


    public PaymentReadDTO rejectPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
            () -> new ResourceNotFoundException("Payment Not Found"));
        payment.setStatus(PaymentStatus.REJETE);
        paymentRepository.save(payment);
        return paymentMapper.toReadDTO(payment);
    }

    }
