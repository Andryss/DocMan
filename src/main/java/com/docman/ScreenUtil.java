package com.docman;

import com.docman.model.ContractModel;
import com.docman.model.PaymentModel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

public class ScreenUtil {

    @SneakyThrows
    public static void openMain() {
        FXMLLoader loader = new FXMLLoader(ScreenUtil.class.getResource(DocManScreen.MAIN.fxmlFile));
        Scene scene = new Scene(loader.load());
        MainViewController controller = loader.getController();
        doOpen(scene, "DocMan", event -> controller.onShown());
    }

    @SneakyThrows
    public static Stage openUpsertContract(ContractModel template) {
        FXMLLoader loader = new FXMLLoader(ScreenUtil.class.getResource(DocManScreen.UPSERT_CONTRACT.fxmlFile));
        Scene scene = new Scene(loader.load());

        UpsertContractViewController controller = loader.getController();
        controller.setTemplate(template);

        return doOpen(scene, (template == null ? "Добавить" : "Редактировать"));
    }

    @SneakyThrows
    public static Stage openUpsertPayment(ContractModel contract, PaymentModel template) {
        FXMLLoader loader = new FXMLLoader(ScreenUtil.class.getResource(DocManScreen.UPSERT_PAYMENT.fxmlFile));
        Scene scene = new Scene(loader.load());

        UpsertPaymentViewController controller = loader.getController();
        controller.setTemplate(contract, template);

        return doOpen(scene, (template == null ? "Добавить" : "Редактировать"));
    }

    @SneakyThrows
    private static Stage doOpen(Scene scene, String title) {
        return doOpen(scene, title, null);
    }

    @SneakyThrows
    private static Stage doOpen(Scene scene, String title, EventHandler<WindowEvent> onShown) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setOnShown(onShown);
        stage.show();
        return stage;
    }

    @RequiredArgsConstructor
    public enum DocManScreen {
        MAIN("main-view.fxml"),
        UPSERT_CONTRACT("upsert-contract-view.fxml"),
        UPSERT_PAYMENT("upsert-payment-view.fxml");

        private final String fxmlFile;
    }
}
