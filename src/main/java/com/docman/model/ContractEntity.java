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
 * Договор
 */
@Getter
@Setter
@Entity
@Table(name = "contracts")
public class ContractEntity {
    /**
     * Идентификатор договора
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    /**
     * Номер договора
     */
    private String number;
    /**
     * Контрагент
     */
    private String agent;
    /**
     * Дата заключения
     */
    private Instant openDate;
    /**
     * Дата окончания
     */
    private Instant closeDate;
    /**
     * Полная сумма
     */
    private long totalValue;
    /**
     * Остаток по договору
     */
    private long remainingValue;
    /**
     * Файл договора
     */
    private String filePath;
    /**
     * Примечание
     */
    private String note;
}
