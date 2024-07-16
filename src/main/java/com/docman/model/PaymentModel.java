package com.docman.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.Instant;

public class PaymentModel {
    private final Long id;
    private final Long contractId;
    private final SimpleObjectProperty<Instant> date;
    private final SimpleLongProperty paymentValue;
    private final SimpleBooleanProperty paid;

    public PaymentModel(Long id, Long contractId, Instant date, long paymentValue, boolean paid) {
        this.id = id;
        this.contractId = contractId;
        this.date = new SimpleObjectProperty<>(date);
        this.paymentValue = new SimpleLongProperty(paymentValue);
        this.paid = new SimpleBooleanProperty(paid);
    }

    public Long getId() {
        return id;
    }

    public Long getContractId() {
        return contractId;
    }

    public Instant getDate() {
        return date.get();
    }

    public SimpleObjectProperty<Instant> dateProperty() {
        return date;
    }

    public void setDate(Instant date) {
        this.date.set(date);
    }

    public long getPaymentValue() {
        return paymentValue.get();
    }

    public SimpleLongProperty paymentValueProperty() {
        return paymentValue;
    }

    public void setPaymentValue(long paymentValue) {
        this.paymentValue.set(paymentValue);
    }

    public boolean isPaid() {
        return paid.get();
    }

    public SimpleBooleanProperty paidProperty() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid.set(paid);
    }
}
