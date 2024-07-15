package com.docman;

import com.docman.model.ContractModel;
import com.docman.model.PaymentEntity;
import com.docman.model.PaymentModel;
import com.docman.repository.ContractRepository;
import com.docman.repository.PaymentRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.docman.AlertUtil.showWarning;

public class UpsertPaymentViewController {
    private final PaymentRepository paymentRepository = PaymentRepository.INSTANCE;
    private final ContractRepository contractRepository = ContractRepository.INSTANCE;

    private ContractModel contract;
    private PaymentModel editingPayment;

    public DatePicker datePicker;
    public TextField paymentValueTextField;

    public void setTemplate(ContractModel contractModel, PaymentModel template) {
        contract = contractModel;
        if (template != null) {
            editingPayment = template;
            datePicker.setValue(LocalDate.ofInstant(template.getDate(), ZoneId.systemDefault()));
            paymentValueTextField.setText(String.valueOf(template.getPaymentValue()));
        }
    }

    public void onSave(ActionEvent event) {
        LocalDate dateValue = datePicker.getValue();
        if (dateValue == null) {
            showWarning("Дата платежа не выбрана");
            return;
        }
        Instant date = dateValue.atStartOfDay(ZoneId.systemDefault()).toInstant();

        double paymentValue;
        try {
            String totalValueStr = paymentValueTextField.getText().strip();
            paymentValue = Double.parseDouble(totalValueStr);
        } catch (NumberFormatException e) {
            showWarning("Неверный формат суммы платежа");
            return;
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setContractId(contract.getId());
        payment.setDate(date);
        payment.setPaymentValue(paymentValue);

        if (editingPayment != null) {
            payment.setId(editingPayment.getId());
            double remainingWithoutPayment = contract.getRemainingValue() + editingPayment.getPaymentValue();
            double newRemaining = remainingWithoutPayment - paymentValue;
            if (newRemaining < 0) {
                showWarning(String.format("Максимальный размер платежа не должен превышать остатка по договору (%s)", remainingWithoutPayment));
                return;
            }
            contractRepository.updateByIdSetRemainingValue(contract.getId(), newRemaining);
            paymentRepository.update(payment);
        } else {
            double remaining = contract.getRemainingValue();
            double newRemaining = remaining - paymentValue;
            if (newRemaining < 0) {
                showWarning(String.format("Максимальный размер платежа не должен превышать остатка по договору (%s)", remaining));
                return;
            }
            contractRepository.updateByIdSetRemainingValue(contract.getId(), newRemaining);
            paymentRepository.save(payment);
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
