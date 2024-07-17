package com.docman;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.*;

public class AlertUtil {
    public static void showWarning(String content) {
        doShow(WARNING, "Ошибка", content);
    }

    public static void showNotification(String content) {
        doShow(INFORMATION, "Уведомление", content);
    }

    public static Optional<ButtonType> showConfirm(String content) {
        return doShow(CONFIRMATION, "Подтверждение", content);
    }

    private static Optional<ButtonType> doShow(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
