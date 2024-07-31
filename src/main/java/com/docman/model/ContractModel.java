package com.docman.model;

import com.docman.util.CurrencyUtil;
import com.docman.util.DateUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class ContractModel {
    private final Long id;
    private final SimpleStringProperty number = new SimpleStringProperty();
    private final SimpleStringProperty agent = new SimpleStringProperty();
    private final SimpleObjectProperty<LocalDate> openDate = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<LocalDate> closeDate = new SimpleObjectProperty<>();
    private final long totalValue;
    private final SimpleObjectProperty<BigDecimal> totalValueDecimal = new SimpleObjectProperty<>();
    private final long remainingValue;
    private final SimpleObjectProperty<BigDecimal> remainingValueDecimal = new SimpleObjectProperty<>();
    private final SimpleStringProperty filePath = new SimpleStringProperty();
    private final SimpleStringProperty note = new SimpleStringProperty();

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
        this.number.set(number);
        this.agent.set(agent);
        this.openDate.set(DateUtil.toLocalDate(openDate));
        this.closeDate.set(DateUtil.toLocalDate(closeDate));
        this.totalValue = totalValue;
        this.totalValueDecimal.set(CurrencyUtil.toDecimal(totalValue));
        this.remainingValue = remainingValue;
        this.remainingValueDecimal.set(CurrencyUtil.toDecimal(remainingValue));
        this.filePath.set(filePath);
        this.note.set(note);
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

    public String getAgent() {
        return agent.get();
    }

    public SimpleStringProperty agentProperty() {
        return agent;
    }

    public LocalDate getOpenDate() {
        return openDate.get();
    }

    public SimpleObjectProperty<LocalDate> openDateProperty() {
        return openDate;
    }

    public LocalDate getCloseDate() {
        return closeDate.get();
    }

    public SimpleObjectProperty<LocalDate> closeDateProperty() {
        return closeDate;
    }

    public long getTotalValue() {
        return totalValue;
    }

    public BigDecimal getTotalValueDecimal() {
        return totalValueDecimal.get();
    }

    public SimpleObjectProperty<BigDecimal> totalValueDecimalProperty() {
        return totalValueDecimal;
    }

    public long getRemainingValue() {
        return remainingValue;
    }

    public BigDecimal getRemainingValueDecimal() {
        return remainingValueDecimal.get();
    }

    public SimpleObjectProperty<BigDecimal> remainingValueDecimalProperty() {
        return remainingValueDecimal;
    }

    public String getFilePath() {
        return filePath.get();
    }

    public SimpleStringProperty filePathProperty() {
        return filePath;
    }

    public String getNote() {
        return note.get();
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }
}
