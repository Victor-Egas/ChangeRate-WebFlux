package com.banca.demo.service;

import com.banca.demo.model.Currency;
import com.banca.demo.model.OperationDataRequest;
import com.banca.demo.model.OperationDataResponse;
import com.banca.demo.repository.CurrencyDao;
import com.banca.demo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyService implements ICurrencyService {

    @Autowired
    private CurrencyDao currencyDao;

    @Override
    public Flux<List<Currency>> getAllCurrency() {
        return Flux.just(currencyDao.findAll());
    }

    @Override
    public Mono<Currency> findByIdCurrency(Integer code) {
        return Mono.just(currencyDao.findById(code).get());
    }

    @Override
    public Mono<Currency> saveCurrency(Currency currency) {
        return Mono.just(currencyDao.save(currency));
    }

    @Override
    public Mono<OperationDataResponse> getOperationDataResponse(
            OperationDataRequest operationRequest) {
        Currency originalCurrency = currencyDao.findById(operationRequest.getCodeOriginalCurrency()).get();
        Currency finalCurrency = currencyDao.findById(operationRequest.getCodeFinalCurrency()).get();


        return Mono.just(operationRequest)
                .map(request -> {
                    OperationDataResponse operationResponse = new OperationDataResponse();
                    operationResponse.setNameCurrency(finalCurrency.getName());
                    operationResponse.setSymbolCurrency(finalCurrency.getSymbol());
                    operationResponse.setChangeType(
                            Util.getChangeType(
                                    originalCurrency.getChangeType(),
                                    finalCurrency.getChangeType()));
                    operationResponse.setOriginalAmount(operationRequest.getOriginalAmount());
                    operationResponse.setFinalAmount(
                            operationRequest.getOriginalAmount().multiply(operationResponse.getChangeType()));

                    return operationResponse;
                }).doOnSuccess(x -> System.out.println("success"))
                .doOnError(e -> System.out.println("error"));
    }
}
