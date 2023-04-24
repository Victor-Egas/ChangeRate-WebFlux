package com.banca.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OperationDataResponse {

    private BigDecimal originalAmount;
    private BigDecimal finalAmount;
    private BigDecimal changeType;
    private String symbolCurrency;
    private String nameCurrency;
}
