package org.smartshop.smartshop.service.Impl;

import org.smartshop.smartshop.exception.*;
import org.smartshop.smartshop.service.ClientService;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.entity.*;
import org.smartshop.smartshop.enums.OrderStatus;
import org.smartshop.smartshop.mapper.OrderItemMapper;
import org.smartshop.smartshop.mapper.OrderMapper;
import org.smartshop.smartshop.repository.*;
import org.smartshop.smartshop.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
        private final OrderRepository orderRepository;
        private final OrderMapper orderMapper;
        private  final ClientRepository clientRepository;
        private final PromoCodeRepository promoCodeRepository;
        private  final OrderItemMapper orderItemMapper;;
        private final ProductRepository productRepository;
        private final OrderItemRepository orderItemRepository;
        private final ConfigRepository configRepository;
        private final ConfigService config;
        private final ClientService clientService;


    @Transactional(readOnly = true)
    public List<OrderReadDTO> getAllOrders(){
        return orderRepository.findAll().stream().map(orderMapper::toReadDTO).toList();
    }
   public  OrderReadDTO createCommande(@Valid OrderCreateDTO orderCreateDTO){
       Client client=clientRepository.findById(orderCreateDTO.getClientId()).orElseThrow(
               ()->new ResourceNotFoundException("Client doesn t existe")
       );
       PromoCode promoCode = null;
       if (orderCreateDTO.getPromoCodeId() != null) {
           promoCode = promoCodeRepository.findById(orderCreateDTO.getPromoCodeId())
                   .orElseThrow(() -> new ResourceNotFoundException("Promo code doesn't exist"));

           if (!promoCode.getIsActive()) {
               throw new BusinessLogicException("Promo Code is Already Used");
           }
       }

       List<OrderItem> listOrderItem= new ArrayList<>();

        for (OrderCreateDTO.OrderItemCreateDTO item : orderCreateDTO.getOrderItems()){
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (item.getQuantity() > product.getAvailableStock()) {
                throw new BusinessLogicException("Insufficient stock for product: " + product.getName());
            }
        }


       BigDecimal subTotalHT = orderCreateDTO.getOrderItems().stream().
               map(item->item.getUnitPriceAtTime().multiply(BigDecimal.valueOf(item.getQuantity())))
               .reduce(BigDecimal.ZERO,BigDecimal::add);



       BigDecimal tierDiscount = switch (client.getLoyaltyTier().name()) {
           case "SILVER" -> new BigDecimal("5");
           case "GOLD" -> new BigDecimal("10");
           case "PLATINUM" -> new BigDecimal("15");
           default -> BigDecimal.ZERO;
       };

       BigDecimal PromoDiscount = (promoCode != null)
               ? promoCode.getDiscountPercentage()
               : BigDecimal.ZERO;
       BigDecimal totalDiscountPercentage=tierDiscount.add(PromoDiscount);


      BigDecimal discountAmount = subTotalHT.multiply(totalDiscountPercentage)
                   .divide(BigDecimal.valueOf(100), 2);

       BigDecimal amountHTAfterDiscount = subTotalHT.subtract(discountAmount);

       BigDecimal tva = amountHTAfterDiscount.multiply(config.getTva());

       BigDecimal grandTotalTTC = amountHTAfterDiscount.add(tva);

        Order order= Order.builder()
                .client(client)
                .status(OrderStatus.PENDING)
                .subTotalHT(subTotalHT)
                .totalDiscount(discountAmount)
                .grandTotalTTC(grandTotalTTC)
                .promoCode(promoCode)
                .remainingAmount(grandTotalTTC)
               // .orderItems(listOrderItem)
                .build();

       orderRepository.save(order);


       for (OrderCreateDTO.OrderItemCreateDTO item : orderCreateDTO.getOrderItems()) {
           Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(()->
                   new ResourceNotFoundException("product doesn t existe")
           );
//           product.setAvailableStock(product.getAvailableStock() - item.getQuantity());
//           productRepository.save(product);

           BigDecimal lineTotal = item.getUnitPriceAtTime()
                   .multiply(BigDecimal.valueOf(item.getQuantity()));

           OrderItem orderItem= OrderItem.builder()
                           .quantity(item.getQuantity())
                           .unitPriceAtTime(item.getUnitPriceAtTime())
                           .product(product)
                           .order(order)
                           .lineTotal(lineTotal)
                           .build();

           listOrderItem.add(orderItem);
           orderItemRepository.save(orderItem);

       }
        order.setOrderItems(listOrderItem);
        orderRepository.save(order);
        return orderMapper.toReadDTO(order);

   }

    @Transactional
    public  OrderReadDTO cancelOrder(Long id){
        Order order=orderRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Order not found")
                );

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Order is already canceled");
        }
        if (order.getStatus() == OrderStatus.REJECTED) {
            throw new IllegalStateException("Cannot cancel a rejected order");
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {

            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = productRepository.findById(orderItem.getProduct().getId()).orElseThrow(() ->
                        new ResourceNotFoundException("Product not Found ")
                );
                product.setAvailableStock(product.getAvailableStock() + orderItem.getQuantity());
                productRepository.save(product);
            }

        }
        if (order.getPromoCode() != null){
            PromoCode promoCode = order.getPromoCode();
            promoCode.setIsActive(true);
            promoCodeRepository.save(promoCode);
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return orderMapper.toReadDTO(order);
    }
    @Transactional
    public  OrderReadDTO rejectOrder(Long id){
        Order order=orderRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Order doesn t existe")
                );

        if (order.getStatus() == OrderStatus.REJECTED) {
            throw new IllegalStateException("Order is already rejected");
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Cannot reject a canceled order");
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {

            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = productRepository.findById(orderItem.getProduct().getId()).orElseThrow(() ->
                        new ResourceNotFoundException("Product doesn t existe")
                );
                product.setAvailableStock(product.getAvailableStock() + orderItem.getQuantity());
                productRepository.save(product);
            }
        }

        if (order.getPromoCode() != null){
            PromoCode promoCode = order.getPromoCode();
            promoCode.setIsActive(true);
            promoCodeRepository.save(promoCode);
        }

        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
        return orderMapper.toReadDTO(order);
    }
    @Transactional
    public  List<OrderReadDTO> getOrderHistoryByClient(Long clientId){
        Client client= clientRepository.findById(clientId).orElseThrow(
                ()->new ResourceNotFoundException("Client with Id Doesn t existe")
        );
        return getAllOrders().stream().filter(order->order.getClient().getId().equals(clientId) &&
                    order.getStatus()==OrderStatus.CONFIRMED
                ).toList();
    }

    public OrderReadDTO validateOrder(Long orderId){
        Order order= orderRepository.findById(orderId).orElseThrow(
                ()->new ResourceNotFoundException("Order Not Found")
        );
        Client client= clientRepository.findById(order.getClient().getId()).orElseThrow(
                ()->new ResourceNotFoundException("CLient Not Found")
        );

        if (order.getStatus()==OrderStatus.CONFIRMED){
            throw new BusinessLogicException("the order already Confirmed");
        }

        PromoCode promoCode = order.getPromoCode();

//      List<OrderReadDTO> clientOrders=  getAllOrders().stream().
//                filter(ordree->ordree.getClient().getId().equals(client.getId()))
//                .filter(ordree->ordree.getStatus()==OrderStatus.PENDING)
//                .toList();

        if (order.getRemainingAmount().compareTo(BigDecimal.ZERO) != 0){
            throw new BusinessLogicException("Order not paid yet");
        }


      for (OrderItem  orderItem : order.getOrderItems()){
          Product product = productRepository.findById(orderItem.getProduct().getId()).orElseThrow(()->
                  new ResourceNotFoundException("product doesn t existe")
          );

          product.setAvailableStock(product.getAvailableStock()-orderItem.getQuantity());
          productRepository.save(product);
      }
      client.setTotalOrders(client.getTotalOrders()+1);
      client.setTotalSpent(client.getTotalSpent().add(order.getSubTotalHT()));
      if (client.getFirstOrderDate()==null){
          client.setFirstOrderDate(LocalDate.now());
      }
      client.setLastOrderDate(LocalDate.now());
      clientService.updateClientTier(client);
      clientRepository.save(client);



        if (promoCode != null){
            promoCode.setIsActive(false);
            promoCodeRepository.save(promoCode);
        }
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);


      return orderMapper.toReadDTO(order);

    }


}
