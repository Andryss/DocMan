package com.docman.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Instant;

public class ContractModel {
    private final Long id;
    private final SimpleStringProperty number;
    private final SimpleObjectProperty<Instant> openDate;
    private final SimpleObjectProperty<Instant> closeDate;
    private final SimpleDoubleProperty totalValue;

    public ContractModel(Long id, String number, Instant openDate, Instant closeDate, double totalValue) {
        this.id = id;
        this.number = new SimpleStringProperty(number);
        this.openDate = new SimpleObjectProperty<>(openDate);
        this.closeDate = new SimpleObjectProperty<>(closeDate);
        this.totalValue = new SimpleDoubleProperty(totalValue);
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

    public double getTotalValue() {
        return totalValue.get();
    }

    public SimpleDoubleProperty totalValueProperty() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue.set(totalValue);
    }
}
