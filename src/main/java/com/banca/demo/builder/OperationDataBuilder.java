package com.banca.demo.builder;

import com.banca.demo.model.Currency;
import com.banca.demo.model.OperationDataRequest;
import com.banca.demo.model.OperationDataResponse;
import com.banca.demo.util.Util;

public class OperationDataBuilder {

    public static OperationDataResponse convertOperationDataResponse(
            OperationDataRequest request, Currency currencyOrigin, Currency currencyFinal) {
        OperationDataResponse operationResponse = new OperationDataResponse();
        operationResponse.setNameCurrency(currencyFinal.getName());
        operationResponse.setSymbolCurrency(currencyFinal.getSymbol());
        operationResponse.setChangeType(
                Util.getChangeType(
                        currencyOrigin.getChangeType(),
                        currencyFinal.getChangeType()));
        operationResponse.setOriginalAmount(request.getOriginalAmount());
        operationResponse.setFinalAmount(
                request.getOriginalAmount().multiply(operationResponse.getChangeType()));

        return operationResponse;
    }
}
