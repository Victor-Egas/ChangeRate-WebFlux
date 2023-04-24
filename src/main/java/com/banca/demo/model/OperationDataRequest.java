package com.banca.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OperationDataRequest {

    private Integer codeOriginalCurrency;
    private Integer codeFinalCurrency;
    private BigDecimal originalAmount;
}
