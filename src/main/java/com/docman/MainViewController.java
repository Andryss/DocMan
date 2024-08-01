package com.docman;

import com.docman.model.ContractModel;
import com.docman.model.NotificationEntity;
import com.docman.model.PaymentModel;
import com.docman.repository.ContractRepository;
import com.docman.repository.NotificationRepository;
import com.docman.repository.PaymentRepository;
import com.docman.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.docman.util.AlertUtil.showWarning;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class MainViewController implements Initializable {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    public TableView<ContractModel> contractTableView;
    public TableView<PaymentModel> paymentTableView;
    public TextField filterTextField;

    private List<ContractModel> fetchedContracts = new ArrayList<>();

    @Setter
    private Function<ContractModel, Stage> openUpsertContractForm;
    @Setter
    private BiFunction<ContractModel, PaymentModel, Stage> openUpsertPaymentForm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initContractTableColumns();
        initPaymentTableColumns();

        contractTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> updatePaymentTable());

        updateContractTable();

        filterTextField.textProperty().addListener((obs, oldVal, newVal) -> filterContractTable());

        contractTableView.setRowFactory(table -> {
            TableRow<ContractModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    doOpenUpsertContractForm(row.getItem());
                }
            });
            return row;
        });

        paymentTableView.setRowFactory(table -> {
            TableRow<PaymentModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    doOpenUpsertPaymentForm(contractTableView.getSelectionModel().getSelectedItem(), row.getItem());
                }
            });
            return row;
        });
    }

    public void onShown() {
        Map<Long, ContractModel> contractsMap = contractTableView.getItems().stream()
                .collect(Collectors.toMap(ContractModel::getId, Function.identity()));
        List<Long> shownIds = new ArrayList<>();
        List<NotificationEntity> batch = new ArrayList<>();
        notificationRepository.findAllNotShownByTimeoutBefore(Instant.now()).forEach(n -> {
            batch.add(n);
            if (batch.size() == 10) {
                StringBuilder builder = new StringBuilder();
                batch.forEach(notification -> {
                    ContractModel contract = contractsMap.get(notification.getContractId());
                    Duration duration = Duration.between(
                            notification.getTimeout(),
                            DateUtil.toInstant(contract.getCloseDate())
                    );
                    builder.append(String.format(
                            "%s) Контракт номер %s контрагент %s заканчивается через %s дня(ей) (дата окончания %s)\n",
                            shownIds.size() + 1, contract.getNumber(), contract.getAgent(), duration.toDays(),
                            contract.getCloseDate()
                    ));
                    shownIds.add(notification.getId());
                });
                builder.setLength(builder.length() - 1);
                AlertUtil.showNotification(builder.toString());
                batch.clear();
            }
        });
        if (!shownIds.isEmpty()) {
            notificationRepository.setShownByIds(shownIds);
        }
    }

    private void initContractTableColumns() {
        TableColumn<ContractModel, String> numberColumn = new TableColumn<>("Номер");
        numberColumn.setCellValueFactory(features -> features.getValue().numberProperty());
        contractTableView.getColumns().add(numberColumn);

        TableColumn<ContractModel, String> agentColumn = new TableColumn<>("Контрагент");
        agentColumn.setCellValueFactory(features -> features.getValue().agentProperty());
        contractTableView.getColumns().add(agentColumn);

        TableColumn<ContractModel, ?> datesSuperColumn = new TableColumn<>("Срок договора");

        TableColumn<ContractModel, LocalDate> openDateColumn = new TableColumn<>("Дата заключения");
        openDateColumn.setCellValueFactory(features -> features.getValue().openDateProperty());
        datesSuperColumn.getColumns().add(openDateColumn);

        TableColumn<ContractModel, LocalDate> closeDateColumn = new TableColumn<>("Дата окончания");
        closeDateColumn.setCellValueFactory(features -> features.getValue().closeDateProperty());
        datesSuperColumn.getColumns().add(closeDateColumn);

        contractTableView.getColumns().add(datesSuperColumn);

        TableColumn<ContractModel, BigDecimal> totalValueColumn = new TableColumn<>("Полная сумма");
        totalValueColumn.setCellValueFactory(features -> features.getValue().totalValueDecimalProperty());
        contractTableView.getColumns().add(totalValueColumn);

        TableColumn<ContractModel, BigDecimal> remainingValueColumn = new TableColumn<>("Остаток");
        remainingValueColumn.setCellValueFactory(features -> features.getValue().remainingValueDecimalProperty());
        remainingValueColumn.setCellFactory(column -> new ColoredDecimalCell<>());
        contractTableView.getColumns().add(remainingValueColumn);

        TableColumn<ContractModel, String> fileColumn = new TableColumn<>("Файл");
        fileColumn.setCellValueFactory(features -> features.getValue().filePathProperty());
        fileColumn.setCellFactory(column -> new FileTableCell<>());
        contractTableView.getColumns().add(fileColumn);

        TableColumn<ContractModel, String> noteColumn = new TableColumn<>("Примечание");
        noteColumn.setCellValueFactory(features -> features.getValue().noteProperty());
        noteColumn.setCellFactory(column -> new TextAreaCell<>());
        contractTableView.getColumns().add(noteColumn);
    }

    private void initPaymentTableColumns() {
        TableColumn<PaymentModel, LocalDate> dateColumn = new TableColumn<>("Дата платежа");
        dateColumn.setCellValueFactory(features -> features.getValue().dateProperty());
        paymentTableView.getColumns().add(dateColumn);

        TableColumn<PaymentModel, BigDecimal> paymentValueColumn = new TableColumn<>("Сумма платежа");
        paymentValueColumn.setCellValueFactory(features -> features.getValue().paymentValueDecimalProperty());
        paymentTableView.getColumns().add(paymentValueColumn);

        TableColumn<PaymentModel, Boolean> paidColumn = new TableColumn<>("Оплата проведена");
        paidColumn.setCellValueFactory(features -> features.getValue().paidProperty());
        paidColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        paymentTableView.getColumns().add(paidColumn);

        paymentTableView.setEditable(true);
    }

    private void updateContractTable() {
        fetchedContracts = contractRepository.findAll().stream()
                .map(e -> new ContractModel(
                        e.getId(), e.getNumber(), e.getAgent(), e.getOpenDate(), e.getCloseDate(),
                        e.getTotalValue(), e.getRemainingValue(), e.getFilePath(), e.getNote()
                ))
                .collect(Collectors.toList());
        filterContractTable();
    }

    private void filterContractTable() {
        String filter = filterTextField.getText().strip().toLowerCase();
        if (filter.isBlank()) {
            contractTableView.setItems(FXCollections.observableList(fetchedContracts));
        } else {
            ObservableList<ContractModel> filteredContracts = FXCollections.observableArrayList();
            fetchedContracts.stream()
                    .filter(contract -> {
                        String number = contract.getNumber();
                        String agent = contract.getAgent();
                        LocalDate openDate = contract.getOpenDate();
                        LocalDate closeDate = contract.getCloseDate();
                        String filePath = contract.getFilePath();
                        String note = contract.getNote();
                        return number != null && number.toLowerCase().contains(filter) ||
                                agent != null && agent.toLowerCase().contains(filter) ||
                                openDate != null && openDate.toString().contains(filter) ||
                                closeDate != null && closeDate.toString().contains(filter) ||
                                contract.getTotalValueDecimal().toString().contains(filter) ||
                                contract.getRemainingValueDecimal().toString().contains(filter) ||
                                filePath != null && filePath.contains(filter) ||
                                note != null && note.contains(filter);
                    })
                    .forEach(filteredContracts::add);
            contractTableView.setItems(filteredContracts);
        }
    }

    private void updateContractTableWithSavedSelection() {
        int selectedIndex = contractTableView.getSelectionModel().getSelectedIndex();
        updateContractTable();
        contractTableView.getSelectionModel().clearAndSelect(selectedIndex);
    }

    private void updatePaymentTable() {
        ContractModel selectedContract = contractTableView.getSelectionModel().getSelectedItem();
        if (selectedContract == null) {
            paymentTableView.setItems(FXCollections.emptyObservableList());
            return;
        }
        ObservableList<PaymentModel> payments = paymentRepository.findAllByContractId(selectedContract.getId()).stream()
                .map(e -> new PaymentModel(e.getId(), e.getContractId(), e.getDate(), e.getPaymentValue(), e.isPaid()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        payments.forEach(payment -> payment.paidProperty().addListener((obs, oldVal, newVal) -> {
            paymentRepository.setPaid(payment.getId(), newVal);
            long add = (newVal ? -payment.getPaymentValue() : payment.getPaymentValue());
            contractRepository.updateByIdAddRemainingValue(payment.getContractId(), add);
            updateContractTableWithSavedSelection();
        }));
        paymentTableView.setItems(payments);
    }

    public void onAddContract() {
        doOpenUpsertContractForm(null);
    }

    public void onEditContract() {
        ContractModel selectedItem = contractTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showWarning("Договор для редактирования не выбран");
            return;
        }
        doOpenUpsertContractForm(selectedItem);
    }

    public void onAddPayment() {
        ContractModel selectedContract = contractTableView.getSelectionModel().getSelectedItem();
        if (selectedContract == null) {
            showWarning("Сначала нужно выбрать контракт");
            return;
        }
        doOpenUpsertPaymentForm(selectedContract, null);
    }

    public void onEditPayment() {
        ContractModel selectedContract = contractTableView.getSelectionModel().getSelectedItem();
        if (selectedContract == null) {
            showWarning("Сначала нужно выбрать контракт");
            return;
        }
        PaymentModel selectedPayment = paymentTableView.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            showWarning("Оплата для редактирования не выбрана");
            return;
        }
        doOpenUpsertPaymentForm(selectedContract, selectedPayment);
    }

    private void doOpenUpsertContractForm(ContractModel editingItem) {
        if (openUpsertContractForm == null) return;
        openUpsertContractForm.apply(editingItem)
                .setOnHiding(event -> updateContractTable());
    }

    private void doOpenUpsertPaymentForm(ContractModel contract, PaymentModel editingPayment) {
        if (openUpsertPaymentForm == null) return;
        openUpsertPaymentForm.apply(contract, editingPayment)
                .setOnHiding(event -> updateContractTableWithSavedSelection());
    }
}
