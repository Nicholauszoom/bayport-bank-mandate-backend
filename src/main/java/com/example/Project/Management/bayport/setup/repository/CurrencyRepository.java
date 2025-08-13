package com.dflex.emkopo.app.setup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dflex.emkopo.app.setup.entity.Currency;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

	Currency findByCurrencyCode(String currencyCode);

}
