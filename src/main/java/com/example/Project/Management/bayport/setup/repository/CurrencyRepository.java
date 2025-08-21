package com.example.Project.Management.bayport.setup.repository;

import com.example.Project.Management.bayport.setup.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

	Currency findByCurrencyCode(String currencyCode);

}
