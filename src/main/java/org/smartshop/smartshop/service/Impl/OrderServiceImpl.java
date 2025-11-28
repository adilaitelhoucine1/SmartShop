package org.smartshop.smartshop.service.Impl;

import org.smartshop.smartshop.exception.InvalidPromoException;
import org.smartshop.smartshop.exception.OrderUnPaidException;
import org.smartshop.smartshop.service.ClientService;
import org.smartshop.smartshop.utils.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.entity.*;
import org.smartshop.smartshop.enums.OrderStatus;
import org.smartshop.smartshop.exception.ResourceNotFoundException;
import org.smartshop.smartshop.exception.StockNotValid;
import org.smartshop.smartshop.mapper.OrderItemMapper;
import org.smartshop.smartshop.mapper.OrderMapper;
import org.smartshop.smartshop.repository.*;
import org.smartshop.smartshop.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
       PromoCode promoCode=promoCodeRepository.findById(orderCreateDTO.getPromoCodeId()).orElseThrow(
               ()->new ResourceNotFoundException("Promo doesn t existe")
       );

       if(!promoCode.getIsActive()){
           throw new InvalidPromoException("Promo Code is Already Used");
       }

       List<OrderItem> listOrderItem= new ArrayList<>();

        for (OrderCreateDTO.OrderItemCreateDTO item : orderCreateDTO.getOrderItems()){
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (item.getQuantity() > product.getAvailableStock()) {
                throw new StockNotValid("Insufficient stock for product: " + product.getName());
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

       BigDecimal PromoDiscount=promoCode.getDiscountPercentage();

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

    @Transactional(readOnly = true)
    public  OrderReadDTO cancelOrder(Long id){
        Order order=orderRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product doesn t existe")
                );
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return orderMapper.toReadDTO(order);
    }
    @Transactional(readOnly = true)
    public  OrderReadDTO rejectOrder(Long id){
        Order order=orderRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product doesn t existe")
                );
        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
        return orderMapper.toReadDTO(order);
    }
    @Transactional(readOnly = true)
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

      List<OrderReadDTO> clientOrders=  getAllOrders().stream().
                filter(ordree->ordree.getClient().getId().equals(client.getId()))
                .filter(ordree->ordree.getStatus()==OrderStatus.PENDING)
                .toList();

      if (!clientOrders.isEmpty() || order.getRemainingAmount().compareTo(BigDecimal.ZERO)!=0){
          throw new OrderUnPaidException("Order not Paid Yet");
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
      clientService.updateClientTier(client);
      clientRepository.save(client);

      return orderMapper.toReadDTO(order);

    }


}
