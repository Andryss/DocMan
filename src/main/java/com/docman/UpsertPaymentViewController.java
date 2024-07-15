package com.docman;

import com.docman.model.PaymentEntity;
import com.docman.model.PaymentModel;
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

    private long contractId;
    private Long editingPaymentId;

    public DatePicker datePicker;
    public TextField paymentValueTextField;

    public void setTemplate(long contractId, PaymentModel template) {
        this.contractId = contractId;
        if (template != null) {
            editingPaymentId = template.getId();
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
        payment.setContractId(contractId);
        payment.setDate(date);
        payment.setPaymentValue(paymentValue);

        if (editingPaymentId != null) {
            payment.setId(editingPaymentId);
            paymentRepository.update(payment);
        } else {
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
