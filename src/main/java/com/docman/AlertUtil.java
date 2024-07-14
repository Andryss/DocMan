package com.docman;

import javafx.scene.control.Alert;

public class AlertUtil {
    public static void showWarning(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
