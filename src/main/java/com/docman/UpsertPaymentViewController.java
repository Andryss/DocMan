package com.docman;

import com.docman.model.ContractModel;
import com.docman.model.PaymentEntity;
import com.docman.model.PaymentModel;
import com.docman.repository.ContractRepository;
import com.docman.repository.PaymentRepository;
import com.docman.util.CurrencyUtil;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.docman.util.AlertUtil.showWarning;

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
            datePicker.setValue(template.getDate());
            paymentValueTextField.setText(template.getPaymentValue().toString());
        }
    }

    public void onSave(ActionEvent event) {
        LocalDate dateValue = datePicker.getValue();
        if (dateValue == null) {
            showWarning("Дата платежа не выбрана");
            return;
        }
        Instant date = dateValue.atStartOfDay(ZoneId.systemDefault()).toInstant();

        long paymentValue;
        try {
            String totalValueStr = paymentValueTextField.getText().strip();
            paymentValue = CurrencyUtil.parseCurrency(totalValueStr);
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
            payment.setPaid(editingPayment.isPaid());
            if (editingPayment.isPaid()) {
                long add = editingPayment.getPaymentValueLong() - paymentValue;
                contractRepository.updateByIdAddRemainingValue(contract.getId(), add);
            }
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
