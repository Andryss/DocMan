package com.docman.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter

@Entity
@Table(name = "contracts")
public class ContractEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String number;
    private String agent;
    private Instant openDate;
    private Instant closeDate;
    private long totalValue;
    private long remainingValue;
    private String note;
}
