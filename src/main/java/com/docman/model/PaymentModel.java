package com.docman.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.Instant;

public class PaymentModel {
    private final Long id;
    private final SimpleObjectProperty<Instant> date;
    private final SimpleLongProperty paymentValue;

    public PaymentModel(Long id, Instant date, long paymentValue) {
        this.id = id;
        this.date = new SimpleObjectProperty<>(date);
        this.paymentValue = new SimpleLongProperty(paymentValue);
    }

    public Long getId() {
        return id;
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
}
