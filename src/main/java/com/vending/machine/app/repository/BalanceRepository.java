package com.vending.machine.app.repository;

import com.vending.machine.app.modal.Balance;
import com.vending.machine.app.modal.BalanceName;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BalanceRepository extends ReactiveMongoRepository<Balance, String> {
    Mono<Balance> findByName(BalanceName name);
}
