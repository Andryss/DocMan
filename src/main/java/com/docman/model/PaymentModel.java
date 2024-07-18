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
    private final SimpleObjectProperty<BigDecimal> paymentValue = new SimpleObjectProperty<>();
    private final long paymentValueLong;
    private final SimpleBooleanProperty paid = new SimpleBooleanProperty();

    public PaymentModel(Long id, long contractId, Instant date, long paymentValue, boolean paid) {
        this.id = id;
        this.contractId = contractId;
        this.date.set(DateUtil.toLocalDate(date));
        this.paymentValue.set(CurrencyUtil.toDecimal(paymentValue));
        this.paymentValueLong = paymentValue;
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

    public BigDecimal getPaymentValue() {
        return paymentValue.get();
    }

    public SimpleObjectProperty<BigDecimal> paymentValueProperty() {
        return paymentValue;
    }

    public long getPaymentValueLong() {
        return paymentValueLong;
    }

    public boolean isPaid() {
        return paid.get();
    }

    public SimpleBooleanProperty paidProperty() {
        return paid;
    }
}
