package com.vending.machine.app.service;

import com.vending.machine.app.dto.BalanceDTO;
import com.vending.machine.app.modal.Balance;
import com.vending.machine.app.modal.BalanceName;
import com.vending.machine.app.repository.BalanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

public class BalanceServiceTest {

    @Mock
    private BalanceRepository repository;

    @Mock
    private TokenService tokenService;

    private BalanceService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new BalanceService(repository, tokenService);
    }

    @Test
    public void getBalanceWithNullName() {
        Assertions.assertThrows(RuntimeException.class, () -> service.getBalanceByName(null), "Balance name cannot be null!");
    }

    @Test
    public void getBalanceWitName() {
        var balance = new Balance("1", BalanceName.CURRENT_SUM, 10);
        Mockito.when(repository.findByName(Mockito.any(BalanceName.class))).thenReturn(Mono.just(balance));
        service.getBalanceByName(BalanceName.CURRENT_SUM).map(foundBalance -> {
           Assertions.assertEquals(balance, foundBalance);
           return foundBalance;
        }).subscribe();
    }

    @Test
    public void addBalanceWithNullRequest() {
        Assertions.assertThrows(RuntimeException.class, () -> service.addToBalance(null), "Null Request!");
    }

    @Test
    public void addBalanceWithNullName() {
        var request = new BalanceDTO(null, 2);
        Assertions.assertThrows(RuntimeException.class, () -> service.addToBalance(request), "Balance name cannot be null!");
    }

    @Test
    public void addNegativeToBalance() {
        var request = new BalanceDTO(BalanceName.CURRENT_SUM, -2);
        Assertions.assertThrows(RuntimeException.class, () -> service.addToBalance(request), "Amount must be positive value!");
    }

    @Test
    public void addBalance() {
        var balance = new Balance("1", BalanceName.CURRENT_SUM, 10);
        var request = new BalanceDTO(BalanceName.CURRENT_SUM, 5);
        Mockito.when(repository.findByName(Mockito.any(BalanceName.class))).thenReturn(Mono.just(balance));
        Mockito.when(repository.save(Mockito.any(Balance.class))).thenReturn(Mono.just(balance));
        service.addToBalance(request).map(updatedBalance -> {
            Assertions.assertEquals(balance, updatedBalance);
            return updatedBalance;
        }).subscribe();
    }

    @Test
    public void enoughBalance() {
        var balance = new Balance("1", BalanceName.CURRENT_SUM, 10);
        Mockito.when(repository.findByName(Mockito.any(BalanceName.class))).thenReturn(Mono.just(balance));
        service.isThereEnoughBalance(7).map(isEnoughBalance -> {
            Assertions.assertTrue(isEnoughBalance);
            return true;
        }).subscribe();
    }

    @Test
    public void notEnoughBalance() {
        var balance = new Balance("1", BalanceName.CURRENT_SUM, 10);
        Mockito.when(repository.findByName(BalanceName.CURRENT_SUM)).thenReturn(Mono.just(balance));
        service.isThereEnoughBalance(12).map(isEnoughBalance -> {
            Assertions.assertTrue(isEnoughBalance);
            return true;
        }).subscribe();
    }

    @Test
    public void withdrawAllFromBalanceWithNullName() {
        Assertions.assertThrows(RuntimeException.class, () -> service.withdrawAllFromBalance(null, null), "Balance name cannot be null!");
    }

    @Test
    public void withdrawAllCurrentSumFromBalanceWithInvalidToken() {
        var initialAmount = 10;
        var balance = new Balance("1", BalanceName.CURRENT_SUM, initialAmount);
        Mockito.when(tokenService.validateToken(Mockito.any(String.class))).thenReturn(Mono.just(false));
        Mockito.when(repository.findByName(BalanceName.CURRENT_SUM)).thenReturn(Mono.just(balance));
        Mockito.when(repository.save(Mockito.any(Balance.class))).thenReturn(Mono.just(balance));
        service.withdrawAllFromBalance(BalanceName.CURRENT_SUM, "invalid").map(withdrewBalance -> {
            Assertions.assertEquals(initialAmount, withdrewBalance.getAmount());
            return withdrewBalance;
        }).subscribe();
    }

    @Test
    public void withdrawAllTotalSumFromBalanceWithValidToken() {
        var initialAmount = 10;
        var balance = new Balance("1", BalanceName.TOTAL_SUM, initialAmount);
        Mockito.when(tokenService.validateToken(Mockito.any(String.class))).thenReturn(Mono.just(true));
        Mockito.when(repository.findByName(BalanceName.TOTAL_SUM)).thenReturn(Mono.just(balance));
        Mockito.when(repository.save(Mockito.any(Balance.class))).thenReturn(Mono.just(balance));
        service.withdrawAllFromBalance(BalanceName.TOTAL_SUM, "valid").map(withdrewBalance -> {
            Assertions.assertEquals(initialAmount, withdrewBalance.getAmount());
            return withdrewBalance;
        }).subscribe();
    }

    @Test
    public void makeSaleWithZeroPrice() {
        Assertions.assertThrows(RuntimeException.class, () -> service.makeSale(0), "Price must be positive value!");
    }
}
