package com.banca.demo.util;

import com.banca.demo.model.OperationDataResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    public static BigDecimal getChangeType (
            BigDecimal originalExchangeRate, BigDecimal finalExchangeRate) {
        BigDecimal exchangeRate = finalExchangeRate.divide(originalExchangeRate, 3, RoundingMode.HALF_UP);
        return exchangeRate;

    }


}
