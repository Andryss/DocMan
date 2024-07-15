package com.docman;

import com.docman.model.ContractModel;
import com.docman.model.PaymentModel;
import com.docman.repository.ContractRepository;
import com.docman.repository.PaymentRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.docman.AlertUtil.showWarning;
import static com.docman.ScreenUtil.openUpsertContract;
import static com.docman.ScreenUtil.openUpsertPayment;

public class MainViewController implements Initializable {
    private final ContractRepository contractRepository = ContractRepository.INSTANCE;
    private final PaymentRepository paymentRepository = PaymentRepository.INSTANCE;

    public TableView<ContractModel> contractTableView;
    public TableView<PaymentModel> paymentTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initContractTableColumns();
        initPaymentTableColumns();

        updateContractTable();
    }

    private void initContractTableColumns() {
        TableColumn<ContractModel, String> numberColumn = new TableColumn<>("Номер договора");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        contractTableView.getColumns().add(numberColumn);

        TableColumn<ContractModel, LocalDate> openDateColumn = new TableColumn<>("Дата открытия");
        openDateColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                LocalDate.ofInstant(features.getValue().getOpenDate(), ZoneId.systemDefault())
        ));
        contractTableView.getColumns().add(openDateColumn);

        TableColumn<ContractModel, LocalDate> closeDateColumn = new TableColumn<>("Дата закрытия");
        closeDateColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                LocalDate.ofInstant(features.getValue().getCloseDate(), ZoneId.systemDefault())
        ));
        contractTableView.getColumns().add(closeDateColumn);

        TableColumn<ContractModel, Double> totalValueColumn = new TableColumn<>("Полная сумма");
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        contractTableView.getColumns().add(totalValueColumn);
    }

    private void initPaymentTableColumns() {
        TableColumn<PaymentModel, LocalDate> dateColumn = new TableColumn<>("Дата платежа");
        dateColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(
                LocalDate.ofInstant(features.getValue().getDate(), ZoneId.systemDefault())
        ));
        paymentTableView.getColumns().add(dateColumn);

        TableColumn<PaymentModel, Double> paymentValueColumn = new TableColumn<>("Платеж");
        paymentValueColumn.setCellValueFactory(new PropertyValueFactory<>("paymentValue"));
        paymentTableView.getColumns().add(paymentValueColumn);
    }

    private void updateContractTable() {
        ObservableList<ContractModel> contracts = contractRepository.findAll().stream()
                .map(e -> new ContractModel(
                        e.getId(), e.getNumber(), e.getOpenDate(), e.getCloseDate(), e.getTotalValue()
                ))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        contractTableView.setItems(contracts);

        contractTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> updatePaymentTable());
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
        openUpsertPayment(selectedContract.getId(), null).setOnHiding(event -> updatePaymentTable());
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
        openUpsertPayment(selectedContract.getId(), selectedPayment).setOnHiding(event -> updatePaymentTable());
    }
}
