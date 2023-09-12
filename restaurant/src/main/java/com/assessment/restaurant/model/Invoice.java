package com.assessment.restaurant.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.*;

/**
 * @author Krishna Chaitanya
 */
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoices")
public class Invoice {

    @Id
    @Column(name = "invoice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "invoice_amount", nullable = false)
    private BigDecimal invoiceAmount;

}
