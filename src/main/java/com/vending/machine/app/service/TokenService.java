package com.vending.machine.app.service;

import com.vending.machine.app.modal.Token;
import com.vending.machine.app.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository repository;

    private final String KEY = "admin123";

    private String generateToken() {
        int length = 128;
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public Mono<String> login(String password) {
        if (!KEY.equals(password)) {
            throw new RuntimeException("Incorrect Key!");
        }
        var token = generateToken();
        var entity = new Token();
        var date = DateUtils.addHours(new Date(), 1);
        entity.setValidUntil(date);
        entity.setToken(token);
        return repository.save(entity).thenReturn(token);
    }

    public Mono<Boolean> validateToken(String token) {
        if (StringUtils.hasText(token)) {
            return repository.findByToken(token).map(entity -> entity.getValidUntil().after(new Date()));
        }
        return Mono.just(false);
    }
}
