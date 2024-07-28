package com.docman.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Класс ячейки таблицы с файлом
 */
@Slf4j
public class FileTableCell<S> extends TableCell<S, String> {
    private final VBox cellBox;
    private final Button fileButton;
    private final FontIcon fileNotFoundIcon;
    private final FontIcon fileOkIcon;

    public FileTableCell() {
        cellBox = new VBox();
        cellBox.setAlignment(Pos.CENTER);

        fileButton = new Button();
        cellBox.getChildren().add(fileButton);

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
            setGraphic(cellBox);
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
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    log.error(String.format("Error during file %s opening", file), e);
                    AlertUtil.showWarning(String.format("Произошла ошибка при открытии файла %s", file.getAbsolutePath()));
                }
                return;
            }
            if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
                AlertUtil.showWarning("Невозможно открыть файл в вашей операционной системе");
                return;
            }
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + file.getAbsolutePath());
            } catch (IOException e) {
                log.error(String.format("Error during file opening in explorer %s", file), e);
                AlertUtil.showWarning(String.format("Невозможно открыть файл %s", file.getAbsolutePath()));
            }
        });
    }

    private void alertFileNotFound(File file) {
        AlertUtil.showWarning(String.format("Файл %s не найден", file.getAbsolutePath()));
    }
}
