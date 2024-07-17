package com.docman.model;

import com.docman.DateUtil;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Instant;
import java.time.LocalDate;

public class ContractModel {
    private final Long id;
    private final SimpleStringProperty number;
    private final SimpleStringProperty agent;
    private final SimpleObjectProperty<LocalDate> openDate;
    private final SimpleObjectProperty<LocalDate> closeDate;
    private final SimpleLongProperty totalValue;
    private final SimpleLongProperty remainingValue;
    private final SimpleStringProperty filePath;
    private final SimpleStringProperty note;

    public ContractModel(
            Long id,
            String number,
            String agent,
            Instant openDate,
            Instant closeDate,
            long totalValue,
            long remainingValue,
            String filePath,
            String note
    ) {
        this.id = id;
        this.number = new SimpleStringProperty(number);
        this.agent = new SimpleStringProperty(agent);
        this.openDate = new SimpleObjectProperty<>(DateUtil.toLocalDate(openDate));
        this.closeDate = new SimpleObjectProperty<>(DateUtil.toLocalDate(closeDate));
        this.totalValue = new SimpleLongProperty(totalValue);
        this.remainingValue = new SimpleLongProperty(remainingValue);
        this.filePath = new SimpleStringProperty(filePath);
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

    public LocalDate getOpenDate() {
        return openDate.get();
    }

    public SimpleObjectProperty<LocalDate> openDateProperty() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate.set(openDate);
    }

    public LocalDate getCloseDate() {
        return closeDate.get();
    }

    public SimpleObjectProperty<LocalDate> closeDateProperty() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
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

    public String getFilePath() {
        return filePath.get();
    }

    public SimpleStringProperty filePathProperty() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
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
