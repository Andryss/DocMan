package com.docman;

import com.docman.model.ContractModel;
import com.docman.model.PaymentModel;
import com.docman.repository.ContractRepository;
import com.docman.repository.NotificationRepository;
import com.docman.repository.PaymentRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.docman.AlertUtil.showWarning;
import static com.docman.ScreenUtil.openUpsertContract;
import static com.docman.ScreenUtil.openUpsertPayment;

public class MainViewController implements Initializable {
    private final ContractRepository contractRepository = ContractRepository.INSTANCE;
    private final PaymentRepository paymentRepository = PaymentRepository.INSTANCE;
    private final NotificationRepository notificationRepository = NotificationRepository.INSTANCE;

    public TableView<ContractModel> contractTableView;
    public TableView<PaymentModel> paymentTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initContractTableColumns();
        initPaymentTableColumns();

        contractTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> updatePaymentTable());

        updateContractTable();
    }

    public void onShown() {
        Map<Long, ContractModel> contractsMap = contractTableView.getItems().stream()
                .collect(Collectors.toMap(ContractModel::getId, Function.identity()));
        List<Long> shownIds = new ArrayList<>();
        notificationRepository.findAllNotShownByTimeoutBefore(Instant.now()).forEach(notification -> {
            ContractModel contract = contractsMap.get(notification.getContractId());
            Duration duration = Duration.between(notification.getTimeout(), contract.getCloseDate());
            AlertUtil.showNotification(contract.getNumber(), contract.getCloseDate(), duration.toDays());
            shownIds.add(notification.getId());
        });
        if (!shownIds.isEmpty()) {
            notificationRepository.setShownByIds(shownIds);
        }
    }

    private void initContractTableColumns() {
        TableColumn<ContractModel, String> numberColumn = new TableColumn<>("Номер договора");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        contractTableView.getColumns().add(numberColumn);

        TableColumn<ContractModel, String> agentColumn = new TableColumn<>("Контрагент");
        agentColumn.setCellValueFactory(new PropertyValueFactory<>("agent"));
        contractTableView.getColumns().add(agentColumn);

        TableColumn<ContractModel, ?> datesSuperColumn = new TableColumn<>("Срок заключения договора");

        TableColumn<ContractModel, LocalDate> openDateColumn = new TableColumn<>("Дата заключения");
        openDateColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                LocalDate.ofInstant(features.getValue().getOpenDate(), ZoneId.systemDefault())
        ));
        datesSuperColumn.getColumns().add(openDateColumn);

        TableColumn<ContractModel, LocalDate> closeDateColumn = new TableColumn<>("Дата окончания");
        closeDateColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                LocalDate.ofInstant(features.getValue().getCloseDate(), ZoneId.systemDefault())
        ));
        datesSuperColumn.getColumns().add(closeDateColumn);

        contractTableView.getColumns().add(datesSuperColumn);

        TableColumn<ContractModel, BigDecimal> totalValueColumn = new TableColumn<>("Полная сумма");
        totalValueColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                CurrencyUtil.toDecimal(features.getValue().getTotalValue())
        ));
        contractTableView.getColumns().add(totalValueColumn);

        TableColumn<ContractModel, BigDecimal> remainingValueColumn = new TableColumn<>("Остаток");
        remainingValueColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                CurrencyUtil.toDecimal(features.getValue().getRemainingValue())
        ));
        contractTableView.getColumns().add(remainingValueColumn);

        TableColumn<ContractModel, String> noteColumn = new TableColumn<>("Примечание");
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        contractTableView.getColumns().add(noteColumn);
    }

    private void initPaymentTableColumns() {
        TableColumn<PaymentModel, LocalDate> dateColumn = new TableColumn<>("Дата платежа");
        dateColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                LocalDate.ofInstant(features.getValue().getDate(), ZoneId.systemDefault())
        ));
        paymentTableView.getColumns().add(dateColumn);

        TableColumn<PaymentModel, BigDecimal> paymentValueColumn = new TableColumn<>("Сумма платежа");
        paymentValueColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                CurrencyUtil.toDecimal(features.getValue().getPaymentValue())
        ));
        paymentTableView.getColumns().add(paymentValueColumn);
    }

    private void updateContractTable() {
        ObservableList<ContractModel> contracts = contractRepository.findAll().stream()
                .map(e -> new ContractModel(
                        e.getId(), e.getNumber(), e.getAgent(), e.getOpenDate(), e.getCloseDate(),
                        e.getTotalValue(), e.getRemainingValue(), e.getNote()
                ))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        contractTableView.setItems(contracts);
    }

    private void updatePaymentTable() {
        ContractModel selectedContract = contractTableView.getSelectionModel().getSelectedItem();
        if (selectedContract == null) {
            paymentTableView.setItems(FXCollections.emptyObservableList());
            return;
        }
        ObservableList<PaymentModel> payments = paymentRepository.findAllByContractId(selectedContract.getId()).stream()
                .map(e -> new PaymentModel(e.getId(), e.getDate(), e.getPaymentValue()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        paymentTableView.setItems(payments);
    }

    public void onAddContract() {
        openUpsertContract(null).setOnHiding(event -> updateContractTable());
    }

    public void onEditContract() {
        ContractModel selectedItem = contractTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showWarning("Договор для редактирования не выбран");
            return;
        }
        openUpsertContract(selectedItem).setOnHiding(event -> updateContractTable());
    }

    public void onAddPayment() {
        ContractModel selectedContract = contractTableView.getSelectionModel().getSelectedItem();
        if (selectedContract == null) {
            showWarning("Сначала нужно выбрать контракт");
            return;
        }
        openUpsertPayment(selectedContract, null).setOnHiding(event -> {
            int selectedIndex = contractTableView.getSelectionModel().getSelectedIndex();
            updateContractTable();
            contractTableView.getSelectionModel().clearAndSelect(selectedIndex);
        });
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
        openUpsertPayment(selectedContract, selectedPayment).setOnHiding(event -> {
            int selectedIndex = contractTableView.getSelectionModel().getSelectedIndex();
            updateContractTable();
            contractTableView.getSelectionModel().clearAndSelect(selectedIndex);
        });
    }
}
