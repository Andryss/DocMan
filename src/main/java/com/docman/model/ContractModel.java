package com.docman.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Instant;

public class ContractModel {
    private final Long id;
    private final SimpleStringProperty number;
    private final SimpleStringProperty agent;
    private final SimpleObjectProperty<Instant> openDate;
    private final SimpleObjectProperty<Instant> closeDate;
    private final SimpleLongProperty totalValue;
    private final SimpleLongProperty remainingValue;
    private final SimpleStringProperty note;

    public ContractModel(
            Long id,
            String number,
            String agent,
            Instant openDate,
            Instant closeDate,
            long totalValue,
            long remainingValue,
            String note
    ) {
        this.id = id;
        this.number = new SimpleStringProperty(number);
        this.agent = new SimpleStringProperty(agent);
        this.openDate = new SimpleObjectProperty<>(openDate);
        this.closeDate = new SimpleObjectProperty<>(closeDate);
        this.totalValue = new SimpleLongProperty(totalValue);
        this.remainingValue = new SimpleLongProperty(remainingValue);
        this.note = new SimpleStringProperty(note);
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

    public String getAgent() {
        return agent.get();
    }

    public SimpleStringProperty agentProperty() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent.set(agent);
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

    public String getNote() {
        return note.get();
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }
}
