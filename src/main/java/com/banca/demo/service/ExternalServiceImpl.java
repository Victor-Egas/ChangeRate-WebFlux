package com.banca.demo.service;

import com.banca.demo.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService{

    private final WebClient.Builder webClientBuilder;

    @Override
    public Flux<ExchangeRate> getCurrencyCodes() {
        log.info("### Entro al service con tiempo : {} ", LocalDateTime.now());
        return webClientBuilder.baseUrl("http://localhost:8083")
                .build()
                .get()
                .uri("/provider/all")
                .retrieve()
                .bodyToFlux(ExchangeRate.class)
                .doOnNext(exchangeRate -> log.info("El objeto consumido es : {}", exchangeRate.toString()))
                .timeout(Duration.ofSeconds(20)) // Timeout de 5 segundos para esta llamada
                .onErrorResume(TimeoutException.class, e -> Flux.error(new SocketTimeoutException())) // Fallback en caso de Timeout
                .doOnError(e -> log.error("### Error en tiempo de espera tiempo : {},  error : {}",
                        LocalDateTime.now(), e.toString()))
                .onErrorResume(e -> Flux.error(new RuntimeException("Error consuming external service")));


    }
}
