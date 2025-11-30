package org.smartshop.smartshop.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.smartshop.smartshop.DTO.payment.PaymentCreateDTO;
import org.smartshop.smartshop.DTO.payment.PaymentReadDTO;
import org.smartshop.smartshop.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
public class PaymentController {
   private final PaymentService  paymentService;

   public PaymentController(PaymentService paymentService){this.paymentService=paymentService;}

    @PostMapping
    public ResponseEntity<PaymentReadDTO> createPayment(@Valid @RequestBody PaymentCreateDTO paymentCreateDTO){
       PaymentReadDTO payment = paymentService.createPayment(paymentCreateDTO);
       return ResponseEntity.ok(payment);
    }

    @GetMapping("{paymentid}/validate")
    public ResponseEntity<PaymentReadDTO> validatePayment(@PathVariable("paymentid") Long id){
        PaymentReadDTO payment=paymentService.validatePayment(id);
        return ResponseEntity.ok(payment);
    }
    @GetMapping("{paymentid}/reject")
    public ResponseEntity<PaymentReadDTO> rejectPayment(@PathVariable("paymentid") Long id){
        PaymentReadDTO payment=paymentService.rejectPayment(id);
        return ResponseEntity.ok(payment);
    }
}
