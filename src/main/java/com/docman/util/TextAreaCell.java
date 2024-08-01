package com.docman.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Класс ячейки таблицы, содержащий большой текст
 */
public class TextAreaCell<S> extends TableCell<S, String> {
    private final VBox cellBox;
    private final Label label;

    public TextAreaCell() {
        cellBox = new VBox();
        cellBox.setAlignment(Pos.CENTER);

        label = new Label();
        cellBox.getChildren().add(label);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null || item.isEmpty()) {
            setGraphic(null);
        } else {
            label.setText(abbreviate(item));
            installTooltip(item);
            setGraphic(cellBox);
        }
    }

    private String abbreviate(String item) {
        int newLineIndex = item.indexOf('\n');
        if (newLineIndex != -1) return item.substring(0, newLineIndex) + "...";
        return item;
    }

    private void installTooltip(String item) {
        Tooltip tooltip = new Tooltip(item);
        tooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(cellBox, tooltip);
    }
}
