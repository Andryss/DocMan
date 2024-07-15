package com.docman;

import com.docman.model.ContractEntity;
import com.docman.model.ContractModel;
import com.docman.repository.ContractRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.docman.AlertUtil.showWarning;

public class UpsertContractViewController {
    private final ContractRepository contractRepository = ContractRepository.INSTANCE;

    private ContractModel editingContract;

    public TextField numberTextField;
    public DatePicker openDatePicker;
    public DatePicker closeDatePicker;
    public TextField totalValueTextField;

    public void setTemplate(ContractModel template) {
        if (template == null) return;
        editingContract = template;
        numberTextField.setText(template.getNumber());
        openDatePicker.setValue(LocalDate.ofInstant(template.getOpenDate(), ZoneId.systemDefault()));
        closeDatePicker.setValue(LocalDate.ofInstant(template.getCloseDate(), ZoneId.systemDefault()));
        totalValueTextField.setText(String.valueOf(template.getTotalValue()));
    }

    public void onSave(ActionEvent event) {
        String number = numberTextField.getText().strip();
        if (number.isBlank()) {
            showWarning("Номер не должен быть пустым");
            return;
        }

        LocalDate openDateValue = openDatePicker.getValue();
        if (openDateValue == null) {
            showWarning("Дата открытия не выбрана");
            return;
        }
        Instant openDate = openDateValue.atStartOfDay(ZoneId.systemDefault()).toInstant();

        LocalDate closeDateValue = closeDatePicker.getValue();
        if (closeDateValue == null) {
            showWarning("Дата закрытия не выбрана");
            return;
        }
        Instant closeDate = closeDateValue.atStartOfDay(ZoneId.systemDefault()).toInstant();

        double totalValue;
        try {
            String totalValueStr = totalValueTextField.getText().strip();
            totalValue = Double.parseDouble(totalValueStr);
        } catch (NumberFormatException e) {
            showWarning("Неверный формат полной стоимости");
            return;
        }

        ContractEntity contract = new ContractEntity();
        contract.setNumber(number);
        contract.setOpenDate(openDate);
        contract.setCloseDate(closeDate);
        contract.setTotalValue(totalValue);
        contract.setRemainingValue(totalValue);

        if (editingContract != null) {
            contract.setId(editingContract.getId());
            double paid = editingContract.getTotalValue() - editingContract.getRemainingValue();
            if (totalValue - paid < 0) {
                showWarning(String.format("Полная стоимость должна быть больше, чем уже оплаченная сумма (%s)", paid));
                return;
            }
            contract.setRemainingValue(contract.getTotalValue() - paid);
            contractRepository.update(contract);
        } else {
            contractRepository.save(contract);
        }

        closeWindow(event);
    }

    public void onCancel(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
