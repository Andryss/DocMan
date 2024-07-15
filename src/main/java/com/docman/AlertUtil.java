package com.docman;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.Alert.AlertType.WARNING;

public class AlertUtil {
    public static void showWarning(String content) {
        doShow(WARNING, "Ошибка", content);
    }

    public static void showNotification(String contractNumber, Instant closeDate, long days) {
        LocalDate date = LocalDate.ofInstant(closeDate, ZoneId.systemDefault());
        doShow(INFORMATION, "Уведомление", String.format(
                "Контракт номер %s заканчивается через %s дня(ей) (дата окончания %s)",
                contractNumber, days, date
        ));
    }

    private static void doShow(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
