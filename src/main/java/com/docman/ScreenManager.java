package com.docman;

import com.docman.model.ContractModel;
import com.docman.model.PaymentModel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ScreenManager {

    private final ApplicationContext context;

    public void openMain() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while opening main form", e);
        }

        MainViewController controller = loader.getController();
        controller.setOpenUpsertContractForm(this::openUpsertContract);
        controller.setOpenUpsertPaymentForm(this::openUpsertPayment);

        doOpen(scene, "DocMan", event -> controller.onShown());
    }

    public Stage openUpsertContract(ContractModel template) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("upsert-contract-view.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while opening upsert contract form", e);
        }

        UpsertContractViewController controller = loader.getController();
        controller.setTemplate(template);

        return doOpen(scene, (template == null ? "Добавить" : "Редактировать"));
    }

    public Stage openUpsertPayment(ContractModel contract, PaymentModel template) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("upsert-payment-view.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while opening upsert payment form", e);
        }

        UpsertPaymentViewController controller = loader.getController();
        controller.setTemplate(contract, template);

        return doOpen(scene, (template == null ? "Добавить" : "Редактировать"));
    }

    private static Stage doOpen(Scene scene, String title) {
        return doOpen(scene, title, null);
    }

    private static Stage doOpen(Scene scene, String title, EventHandler<WindowEvent> onShown) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setOnShown(onShown);
        stage.show();
        return stage;
    }
}
