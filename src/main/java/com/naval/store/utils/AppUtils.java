package com.naval.store.utils;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AppUtils {
    public long getAmountInPaisa(BigDecimal amount) {
        BigDecimal paiseDecimal = amount.multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_UP);
        return paiseDecimal.longValue();
    }
}
