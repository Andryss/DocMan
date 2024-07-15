package com.docman.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Instant;

public class ContractModel {
    private final Long id;
    private final SimpleStringProperty number;
    private final SimpleObjectProperty<Instant> openDate;
    private final SimpleObjectProperty<Instant> closeDate;
    private final SimpleLongProperty totalValue;
    private final SimpleLongProperty remainingValue;

    public ContractModel(Long id, String number, Instant openDate, Instant closeDate, long totalValue, long remainingValue) {
        this.id = id;
        this.number = new SimpleStringProperty(number);
        this.openDate = new SimpleObjectProperty<>(openDate);
        this.closeDate = new SimpleObjectProperty<>(closeDate);
        this.totalValue = new SimpleLongProperty(totalValue);
        this.remainingValue = new SimpleLongProperty(remainingValue);
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number.get();
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public Instant getOpenDate() {
        return openDate.get();
    }

    public SimpleObjectProperty<Instant> openDateProperty() {
        return openDate;
    }

    public void setOpenDate(Instant openDate) {
        this.openDate.set(openDate);
    }

    public Instant getCloseDate() {
        return closeDate.get();
    }

    public SimpleObjectProperty<Instant> closeDateProperty() {
        return closeDate;
    }

    public void setCloseDate(Instant closeDate) {
        this.closeDate.set(closeDate);
    }

    public long getTotalValue() {
        return totalValue.get();
    }

    public SimpleLongProperty totalValueProperty() {
        return totalValue;
    }

    public void setTotalValue(long totalValue) {
        this.totalValue.set(totalValue);
    }

    public long getRemainingValue() {
        return remainingValue.get();
    }

    public SimpleLongProperty remainingValueProperty() {
        return remainingValue;
    }

    public void setRemainingValue(long remainingValue) {
        this.remainingValue.set(remainingValue);
    }
}
