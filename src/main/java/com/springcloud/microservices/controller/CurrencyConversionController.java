package com.springcloud.microservices.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springcloud.microservices.model.CurrencyConversion;
import com.springcloud.microservices.proxy.CurrencyExchangeProxy;

@RestController
public class CurrencyConversionController {

	@Autowired
	Environment environment;
	
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{qunatity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("qunatity") BigDecimal qunatity) {

		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversion> resposeEntity = new RestTemplate().getForEntity("http://localhost:8001/currency-exchange/from/{from}/to/{to}",
				CurrencyConversion.class, uriVariables);
		CurrencyConversion currencyConversion = resposeEntity.getBody();
		
		return  new CurrencyConversion(currencyConversion.getId(), from, to, qunatity, currencyConversion.getConversionMultiple(),
				qunatity.multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());

	}
	
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{qunatity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("qunatity") BigDecimal qunatity) {

		CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveCurrencyExchange(from, to);
		return  new CurrencyConversion(currencyConversion.getId(), from, to, qunatity, currencyConversion.getConversionMultiple(),
				qunatity.multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());

	}

}
