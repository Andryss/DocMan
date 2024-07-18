package com.docman.util;

import javafx.scene.control.TableCell;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class ColoredDecimalCell<S> extends TableCell<S, BigDecimal> {
    private static final String EMPTY_STYLE = "";
    private static final String RED_COLORED_TEXT_STYLE = "-fx-text-fill: red";

    @Override
    protected void updateItem(BigDecimal item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setStyle(EMPTY_STYLE);
        } else {
            setText(item.toString());
            if (item.compareTo(ZERO) < 0) {
                setStyle(RED_COLORED_TEXT_STYLE);
            } else {
                setStyle(EMPTY_STYLE);
            }
        }
    }
}
