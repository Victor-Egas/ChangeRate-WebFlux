package com.banca.demo.service;

import com.banca.demo.model.ExchangeRate;
import reactor.core.publisher.Flux;

public interface ExternalService {

    Flux<ExchangeRate> getCurrencyCodes();
}
