package com.docman;

import com.docman.model.ContractModel;
import com.docman.repository.ContractRepository;
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

import static com.docman.ScreenUtil.openUpsertContract;

public class MainViewController implements Initializable {
    private final ContractRepository contractRepository = ContractRepository.INSTANCE;

    public TableView<ContractModel> contractTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        updateContractTable();
    }

    public void onAdd() {
        openUpsertContract(null).setOnHiding(event -> updateContractTable());
    }

    private void updateContractTable() {
        ObservableList<ContractModel> contracts = contractRepository.findAll().stream()
                .map(e -> new ContractModel(
                        e.getId(), e.getNumber(), e.getOpenDate(), e.getCloseDate(), e.getTotalValue()
                ))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        contractTableView.setItems(contracts);
    }

    public void onEdit() {
        ContractModel selectedItem = contractTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            AlertUtil.showWarning("Договор для редактирования не выбран");
            return;
        }
        openUpsertContract(selectedItem).setOnHiding(event -> updateContractTable());
    }
}
