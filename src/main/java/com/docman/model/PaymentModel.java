package com.docman.model;

import com.docman.util.CurrencyUtil;
import com.docman.util.DateUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class PaymentModel {
    private final Long id;
    private final long contractId;
    private final SimpleObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final long paymentValue;
    private final SimpleObjectProperty<BigDecimal> paymentValueDecimal = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty paid = new SimpleBooleanProperty();

    public PaymentModel(Long id, long contractId, Instant date, long paymentValueDecimal, boolean paid) {
        this.id = id;
        this.contractId = contractId;
        this.date.set(DateUtil.toLocalDate(date));
        this.paymentValue = paymentValueDecimal;
        this.paymentValueDecimal.set(CurrencyUtil.toDecimal(paymentValueDecimal));
        this.paid.set(paid);
    }

    public Long getId() {
        return id;
    }

    public long getContractId() {
        return contractId;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public long getPaymentValue() {
        return paymentValue;
    }

    public BigDecimal getPaymentValueDecimal() {
        return paymentValueDecimal.get();
    }

    public SimpleObjectProperty<BigDecimal> paymentValueDecimalProperty() {
        return paymentValueDecimal;
    }

    public boolean isPaid() {
        return paid.get();
    }

    public SimpleBooleanProperty paidProperty() {
        return paid;
    }
}
