package com.banca.demo.repository;

import com.banca.demo.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyDao extends JpaRepository<Currency, Integer> {
}
