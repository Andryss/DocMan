package com.docman.util;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileTableCell<S> extends TableCell<S, String> {
    private final Button fileButton;
    private final FontIcon fileNotFoundIcon;
    private final FontIcon fileOkIcon;

    public FileTableCell() {
        fileButton = new Button();
        fileButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        fileNotFoundIcon = new FontIcon(MaterialDesignA.ALERT);
        fileNotFoundIcon.setIconSize(16);

        fileOkIcon = new FontIcon(MaterialDesignF.FILE);
        fileOkIcon.setIconSize(16);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            File file = new File(item);
            if (!file.exists() || file.isDirectory()) {
                onFileNotFound(file);
            } else {
                onFileOk(file);
            }
            setGraphic(fileButton);
        }
    }

    private void onFileNotFound(File file) {
        fileButton.setGraphic(fileNotFoundIcon);
        fileButton.setOnAction(event -> alertFileNotFound(file));
    }

    private void onFileOk(File file) {
        fileButton.setGraphic(fileOkIcon);
        fileButton.setOnAction(event -> {
            if (!file.exists() || file.isDirectory()) {
                alertFileNotFound(file);
                onFileNotFound(file);
                return;
            }
            if (!Desktop.isDesktopSupported()) {
                AlertUtil.showWarning(String.format("Невозможно открыть файл %s", file.getAbsolutePath()));
                return;
            }
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                AlertUtil.showWarning(String.format("Произошла ошибка при открытии файла %s", file.getAbsolutePath()));
                e.printStackTrace();
            }
        });
    }

    private void alertFileNotFound(File file) {
        AlertUtil.showWarning(String.format("Файл %s не найден", file.getAbsolutePath()));
    }
}
