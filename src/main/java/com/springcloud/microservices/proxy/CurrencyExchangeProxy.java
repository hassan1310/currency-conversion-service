package com.springcloud.microservices.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springcloud.microservices.model.CurrencyConversion;

//@FeignClient(name="currency-exchange",url="http://localhost:8000")
@FeignClient(name="currency-exchange")
public interface CurrencyExchangeProxy {
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversion retrieveCurrencyExchange(@PathVariable("from") String from,
			@PathVariable("to") String to) ;
	

}
