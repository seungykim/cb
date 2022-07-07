package com.seungkim.cb;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;

public class AlbumService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumService.class);

    private final CircuitBreaker circuitBreaker;

    public AlbumService(final CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    public String getAlbumList(int waitSec) {
        Supplier<String> restSupplier = () -> {
            LOGGER.info("[EXTERNAL CALL] Trying the external call...");
            try {
                if (waitSec > 0) {
                    Thread.sleep(waitSec * 1000);
                }
            } catch (Exception ex) {
            }

            return getMainAlbumList();
        };

        Supplier<String> decorated = Decorators
                .ofSupplier(restSupplier)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(ex -> getDefaultAlbumList(ex)).decorate();
        return decorated.get();
    }

    public String getAlbumList(boolean throwException) {
        Supplier<String> restSupplier = () -> {
            LOGGER.info("[EXTERNAL CALL] Trying the external call...");
            if (throwException) {
                throw new RuntimeException("[EXTERNAL CALL EXCEPTION] Eventually circuit breaker should open");
            }

            return getMainAlbumList();
        };

        Supplier<String> decorated = Decorators
                .ofSupplier(restSupplier)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(ex -> getDefaultAlbumList(ex)).decorate();
        return decorated.get();
    }

    private String getDefaultAlbumList(Throwable ex) {
        LOGGER.error("[Fallback] Circuit Breaker Open");
        return getResourceFile("fallback-data.json");
    }

    private String getMainAlbumList() {
        return getResourceFile("main-data.json");
    }
    
    private String getResourceFile(String fileName) {
        try {
            return Files.readString(Paths.get(getClass().getClassLoader().getResource(fileName).toURI()));
        } catch (Exception e) {
        }
        return null;
    }
}
