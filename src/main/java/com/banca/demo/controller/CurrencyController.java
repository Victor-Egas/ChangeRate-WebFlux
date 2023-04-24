package com.banca.demo.controller;

import com.banca.demo.model.Currency;
import com.banca.demo.model.OperationDataRequest;
import com.banca.demo.model.OperationDataResponse;
import com.banca.demo.service.CurrencyService;
import com.banca.demo.service.ICurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private ICurrencyService service;

    @GetMapping("/all")
    public Mono<ResponseEntity<Flux<List<Currency>>>> getAllCurrencyType() {
        System.out.println("ENTRO");
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAllCurrency()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<Currency>>> getByIdCurrency
            (@PathVariable Integer id) {
        System.out.println("ENTRO");
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findByIdCurrency(id)));
    }

    @PostMapping("/insert")
    public Mono<ResponseEntity<Mono<Currency>>> saveCurrency(
            @RequestBody Currency currency) {
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.saveCurrency(currency)));

    }

    @PostMapping("/update")
    public Mono<ResponseEntity<Mono<Currency>>> updateCurrency(
            @RequestBody Currency currency) {
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.saveCurrency(currency)));

    }

    @PostMapping("/changeType")
    public Mono<ResponseEntity<Mono<OperationDataResponse>>> getExchangeRateAmount(
            @RequestBody OperationDataRequest operationRequest) {
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getOperationDataResponse(operationRequest)));
    }
}
