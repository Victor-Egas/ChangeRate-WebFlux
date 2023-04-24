package com.banca.demo.service;

import com.banca.demo.model.Currency;
import com.banca.demo.model.OperationDataRequest;
import com.banca.demo.model.OperationDataResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICurrencyService {
    Flux<List<Currency>> getAllCurrency();
    Mono<Currency> findByIdCurrency(Integer id);
    Mono<Currency> saveCurrency(Currency currency);
    Mono<OperationDataResponse> getOperationDataResponse(OperationDataRequest operationRequest);
}
