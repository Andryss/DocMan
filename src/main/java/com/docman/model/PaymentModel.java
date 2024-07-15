package com.docman.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.Instant;

public class PaymentModel {
    private final Long id;
    private final SimpleObjectProperty<Instant> date;
    private final SimpleDoubleProperty paymentValue;

    public PaymentModel(Long id, Instant date, double paymentValue) {
        this.id = id;
        this.date = new SimpleObjectProperty<>(date);
        this.paymentValue = new SimpleDoubleProperty(paymentValue);
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

    public double getPaymentValue() {
        return paymentValue.get();
    }

    public SimpleDoubleProperty paymentValueProperty() {
        return paymentValue;
    }

    public void setPaymentValue(double paymentValue) {
        this.paymentValue.set(paymentValue);
    }
}
