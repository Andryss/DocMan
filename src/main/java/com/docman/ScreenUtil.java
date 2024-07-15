package com.docman;

import com.docman.model.ContractModel;
import com.docman.model.PaymentModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

public class ScreenUtil {

    @SneakyThrows
    public static void open(DocManScreen screen) {
        FXMLLoader loader = new FXMLLoader(ScreenUtil.class.getResource(screen.fxmlFile));
        doOpen(new Scene(loader.load()), "DocMan");
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
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
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
