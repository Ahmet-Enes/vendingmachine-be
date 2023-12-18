package com.vending.machine.app.repository;

import com.vending.machine.app.modal.Token;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface TokenRepository extends ReactiveMongoRepository<Token, String> {
    Mono<Token> findByToken(String token);
    Flux<Token> findByValidUntilBefore(Date date);
}
