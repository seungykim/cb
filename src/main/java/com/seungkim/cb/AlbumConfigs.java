package com.seungkim.cb;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;

@Configuration
public class AlbumConfigs {
	
	@Bean
	public AlbumService getAlbumService(@Qualifier("circuitBreaker") CircuitBreaker circuitBreaker) {
		return new AlbumService(circuitBreaker);
	}
	
	@Bean("circuitBreaker")
	public CircuitBreaker circuitBreaker() {
		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
				.slidingWindow(5, 5, SlidingWindowType.COUNT_BASED) // look-back last 5 calls. Wait for 5 initial calls to determine the result.
				.permittedNumberOfCallsInHalfOpenState(2) // during the HALF-OPEN state, allow 2 out-bound call
				.slowCallRateThreshold(50) // or slow calls are more than 50%
				.slowCallDurationThreshold(Duration.ofMillis(1500)) // this is the slow call threshold (1.5s)
				.waitDurationInOpenState(Duration.ofSeconds(10)) // Wait at least 10 seconds in the OPEN state => transition to HALF-OPEN
				.maxWaitDurationInHalfOpenState(Duration.ofSeconds(10))
				.writableStackTraceEnabled(false) // don't print verbose exception thrown by the circuit breaker.
				.build();

		CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

		return circuitBreakerRegistry.circuitBreaker(SlidingWindowType.COUNT_BASED + "CircuitBreaker", circuitBreakerConfig);
	}
}
