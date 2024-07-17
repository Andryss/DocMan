package com.docman;

import com.docman.model.ContractEntity;
import com.docman.model.ContractModel;
import com.docman.model.NotificationEntity;
import com.docman.repository.ContractRepository;
import com.docman.repository.NotificationRepository;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.docman.AlertUtil.showWarning;
import static java.time.temporal.ChronoUnit.DAYS;

public class UpsertContractViewController implements Initializable {
    private final ContractRepository contractRepository = ContractRepository.INSTANCE;
    private final NotificationRepository notificationRepository = NotificationRepository.INSTANCE;

    private ContractModel editingContract;

    public TextField numberTextField;
    public TextField agentTextField;
    public DatePicker openDatePicker;
    public DatePicker closeDatePicker;
    public TextField totalValueTextField;
    public CheckBox oneDayBeforeCheckbox;
    public CheckBox twoDaysBeforeCheckbox;
    public CheckBox threeDaysBeforeCheckbox;
    public CheckBox fiveDaysBeforeCheckbox;
    public CheckBox tenDaysBeforeCheckbox;
    public TextField filePathTextField;
    public TextArea noteTextArea;

    private Map<CheckBox, Long> checkboxesDaysTimeout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkboxesDaysTimeout = Map.of(
                oneDayBeforeCheckbox, 1L,
                twoDaysBeforeCheckbox, 2L,
                threeDaysBeforeCheckbox, 3L,
                fiveDaysBeforeCheckbox, 5L,
                tenDaysBeforeCheckbox, 10L
        );
    }

    public void setTemplate(ContractModel template) {
        if (template == null) return;
        editingContract = template;
        numberTextField.setText(template.getNumber());
        agentTextField.setText(template.getAgent());
        openDatePicker.setValue(template.getOpenDate());
        closeDatePicker.setValue(template.getCloseDate());
        totalValueTextField.setText(CurrencyUtil.toDecimal(template.getTotalValue()).toString());

        notificationRepository.findAllByContractId(template.getId()).forEach(notification -> {
            long duration = Duration.between(
                    notification.getTimeout(),
                    DateUtil.toInstant(template.getCloseDate())
            ).toDays();
            checkboxesDaysTimeout.forEach((checkBox, days) -> {
                if (duration == days) checkBox.setSelected(true);
            });
        });

        noteTextArea.setText(template.getNote());
    }

    public void onSave(ActionEvent event) {
        String number = numberTextField.getText().strip();
        if (number.isBlank()) {
            showWarning("Номер не должен быть пустым");
            return;
        }

        String agent = agentTextField.getText().strip();
        if (agent.isBlank()) {
            showWarning("Контрагент не должен быть пустым");
            return;
        }

        LocalDate openDateValue = openDatePicker.getValue();
        if (openDateValue == null) {
            showWarning("Дата открытия не выбрана");
            return;
        }
        Instant openDate = DateUtil.toInstant(openDateValue);

        LocalDate closeDateValue = closeDatePicker.getValue();
        if (closeDateValue == null) {
            showWarning("Дата закрытия не выбрана");
            return;
        }
        Instant closeDate = DateUtil.toInstant(closeDateValue);

        long totalValue;
        try {
            String totalValueStr = totalValueTextField.getText().strip();
            totalValue = CurrencyUtil.parseCurrency(totalValueStr);
        } catch (NumberFormatException e) {
            showWarning("Неверный формат полной стоимости");
            return;
        }

        String filePathValue = filePathTextField.getText().strip();
        String filePath = (filePathValue.isEmpty() ? null : filePathValue);

        String note = noteTextArea.getText().strip();

        ContractEntity contract = new ContractEntity();
        contract.setNumber(number);
        contract.setAgent(agent);
        contract.setOpenDate(openDate);
        contract.setCloseDate(closeDate);
        contract.setTotalValue(totalValue);
        contract.setRemainingValue(totalValue);
        contract.setFilePath(filePath);
        contract.setNote(note);

        if (editingContract != null) {
            contract.setId(editingContract.getId());
            long paid = editingContract.getTotalValue() - editingContract.getRemainingValue();
            long newRemaining = totalValue - paid;
            contract.setRemainingValue(newRemaining);
            contractRepository.update(contract);
        } else {
            contractRepository.save(contract);
        }
        saveNewNotifications(contract);

        closeWindow(event);
    }

    private void saveNewNotifications(ContractEntity contract) {
        notificationRepository.deleteAllNotShownByContractId(contract.getId());

        List<NotificationEntity> notifications = new ArrayList<>();
        Instant now = Instant.now();
        checkboxesDaysTimeout.forEach((checkBox, days) -> {
            Instant timeout = contract.getCloseDate().minus(days, DAYS);
            if (checkBox.isSelected() && timeout.isAfter(now)) {
                NotificationEntity n = new NotificationEntity();
                n.setContractId(contract.getId());
                n.setTimeout(timeout);
                notifications.add(n);
            }
        });

        notificationRepository.saveAll(notifications);
    }

    public void onCancel(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    public void onChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file == null) return;
        filePathTextField.setText(file.getAbsolutePath());
    }
}
