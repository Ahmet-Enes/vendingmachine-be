package com.vending.machine.app.service;

import com.vending.machine.app.modal.Token;
import com.vending.machine.app.repository.TokenRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Date;

public class TokenServiceTest {

    @Mock
    private TokenRepository repository;

    private TokenService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new TokenService(repository);
    }

    @Test
    public void loginWithWrongPassword() {
        Assertions.assertThrows(RuntimeException.class, () -> service.login("wrong"), "Incorrect Key!");
    }

    @Test
    public void login() {
        var entity = new Token();
        Mockito.when(repository.save(Mockito.any(Token.class))).thenReturn(Mono.just(entity));
        service.login("admin123").map(generatedToken -> {
            Assertions.assertNotNull(generatedToken);
            return generatedToken;
        }).subscribe();
    }

    @Test
    public void validateToken() {
        var entity = new Token();
        var token = "token";
        entity.setValidUntil(DateUtils.addHours(new Date(), 1));
        Mockito.when(repository.findByToken(token)).thenReturn(Mono.just(entity));
        service.validateToken(token).map(isValid -> {
            Assertions.assertTrue(isValid);
            return true;
        }).subscribe();
    }
}
