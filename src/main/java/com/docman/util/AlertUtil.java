package com.docman.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.*;

/**
 * Вспомогательный класс для работы с уведомлениями (alert)
 */
public class AlertUtil {
    public static void showWarning(String content) {
        doShow(WARNING, "Ошибка", content);
    }

    public static void showNotification(String content) {
        doShow(INFORMATION, "Уведомление", content);
    }

    private static Optional<ButtonType> doShow(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
