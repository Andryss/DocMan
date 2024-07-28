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
 * Уведомление
 */
@Getter
@Setter
@Entity
@Table(name = "notifications")
public class NotificationEntity {
    /**
     * Идентификатор уведомления
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    /**
     * Идентификатор договора
     */
    private long contractId;
    /**
     * Время срабатывания уведомления
     */
    private Instant timeout;
    /**
     * Флаг, если true - уведомление показано
     */
    private boolean isShown;
}
