package org.smartshop.smartshop.entity;
import jakarta.persistence.*;
import lombok.*;
import org.smartshop.smartshop.enums.PaymentStatus;
import org.smartshop.smartshop.enums.PaymentType;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.EN_ATTENTE;
    @Column(nullable = false)
    private String reference;
    private LocalDate dueDate;
    private String bankName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
