package com.banca.demo.service;

import com.banca.demo.builder.OperationDataBuilder;
import com.banca.demo.model.Currency;
import com.banca.demo.model.OperationDataRequest;
import com.banca.demo.model.OperationDataResponse;
import com.banca.demo.repository.CurrencyDao;
import com.banca.demo.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        log.info("Inicia el Service getOperationDataResponse()");
        Currency originalCurrency = currencyDao.findById(operationRequest.getCodeOriginalCurrency()).get();
        Currency finalCurrency = currencyDao.findById(operationRequest.getCodeFinalCurrency()).get();


        return Mono.zip(Mono.just(currencyDao.findById(operationRequest.getCodeOriginalCurrency()).get()),
                Mono.just(currencyDao.findById(operationRequest.getCodeFinalCurrency()).get()))
               .map(result -> OperationDataBuilder.convertOperationDataResponse(
                       operationRequest, result.getT1(), result.getT2()))
                .doOnSuccess(x -> log.info("Se realizo el tipo de cambio con exito"))
                .doOnError(e -> log.error("Ocurrio un error en el tipo de cambio, error {}", e.getMessage()))
                .doFinally(signalType -> log.info("Finalizo el SERVICE getOperationDataResponse()"));
    }
}
