package com.vending.machine.app.service;

import com.vending.machine.app.dto.BalanceDTO;
import com.vending.machine.app.exception.UnauthorizedAccessException;
import com.vending.machine.app.modal.Balance;
import com.vending.machine.app.modal.BalanceName;
import com.vending.machine.app.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository repository;
    private final TokenService tokenService;

    public Mono<Balance> getBalanceByName(BalanceName name) {
        if (Objects.isNull(name)) {
            throw new RuntimeException("Balance name cannot be null!");
        }
        return repository.findByName(name);
    }

    public Mono<Balance> addToBalance(BalanceDTO request) {
        if (Objects.isNull(request)) {
            throw new RuntimeException("Null Request!");
        }
        checkBalanceDTOName(request);
        checkBalanceDTOAmount(request);
        return repository.findByName(request.getName()).flatMap(entity -> {
           entity.setAmount(entity.getAmount() + request.getAmount());
           return repository.save(entity);
        });
    }

    public Mono<Boolean> isThereEnoughBalance(int price) {
        return repository.findByName(BalanceName.CURRENT_SUM)
                .map(balance -> price <= balance.getAmount());
    }

    private void checkBalanceDTOAmount(BalanceDTO dto) {
        if (dto.getAmount() <= 0) {
            throw new RuntimeException("Amount must be positive value!");
        }
    }

    private void checkBalanceDTOName(BalanceDTO dto) {
        if (Objects.isNull(dto.getName())) {
            throw new RuntimeException("Balance name cannot be null!");
        }
    }

    public Mono<BalanceDTO> withdrawAllFromBalance(BalanceName name, String token) {
        if (Objects.isNull(name)) {
            throw new RuntimeException("Balance name cannot be null!");
        }
        if (BalanceName.TOTAL_SUM.equals(name)) {
            return tokenService.validateToken(token).flatMap(isValid -> {
                if (isValid) {
                    return withdrawMoney(name);
                } else {
                    throw new UnauthorizedAccessException("Token Invalid!");
                }
            });
        }
        return withdrawMoney(name);
    }

    private Mono<BalanceDTO> withdrawMoney(BalanceName name) {
        return repository.findByName(name).flatMap(entity -> {
            var withdrewBalance = new BalanceDTO(entity.getName(), entity.getAmount());
            entity.setAmount(0);
            return repository.save(entity).thenReturn(withdrewBalance);
        });
    }

    public Mono<BalanceDTO> makeSale(int price) {
        if (price <= 0) {
            throw new RuntimeException("Price must be positive value!");
        }
        return withdrawAllFromBalance(BalanceName.CURRENT_SUM, null).flatMap(currentBalance -> {
            var change = new BalanceDTO(BalanceName.CURRENT_SUM, currentBalance.getAmount() - price);
            var dto = new BalanceDTO(BalanceName.TOTAL_SUM, price);
            return addToBalance(dto).thenReturn(change);
        });
    }
}
