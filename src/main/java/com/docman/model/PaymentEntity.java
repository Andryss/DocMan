package com.docman.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Платеж
 */
@Getter
@Setter
@Entity
@Table(name = "payments")
public class PaymentEntity {
    /**
     * Идентификатор платежа
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    /**
     * Идентификатор договора
     */
    private long contractId;
    /**
     * Дата платежа
     */
    private Instant date;
    /**
     * Сумма платежа
     */
    private long paymentValue;
    /**
     * Флаг, если true - оплата проведена
     */
    private boolean paid;
}
