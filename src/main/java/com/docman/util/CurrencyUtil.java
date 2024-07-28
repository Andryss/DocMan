package com.docman.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Вспомогательный класс для работы с денежными величинами
 */
public class CurrencyUtil {
    public static long parseCurrency(String text) throws NumberFormatException {
        char[] chars = text.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c) && c != '.') throw new NumberFormatException("only digits and dot");
        }
        String[] parts = text.split("\\.");
        if (parts.length > 2) throw new NumberFormatException("more than one dot");
        long rubles = Long.parseLong(parts[0]);
        if (parts.length == 2) {
            if (parts[1].length() > 2) throw new NumberFormatException("only two digits after dot");
            long kops = Long.parseLong(parts[1]);
            if (parts[1].length() == 1) kops *= 10;
            return rubles * 100 + kops;
        } else {
            return rubles * 100;
        }
    }
    public static BigDecimal toDecimal(long currency) {
        return BigDecimal.valueOf(currency).setScale(2, RoundingMode.UNNECESSARY)
                .divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY);
    }
}
