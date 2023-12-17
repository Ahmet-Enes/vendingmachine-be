package com.vending.machine.app.controller;

import com.vending.machine.app.dto.BalanceDTO;
import com.vending.machine.app.modal.Balance;
import com.vending.machine.app.modal.BalanceName;
import com.vending.machine.app.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balance")
public class BalanceController {
    private final BalanceService service;

    @GetMapping("/{name}")
    public Mono<Balance> getBalanceByName(@PathVariable BalanceName name) {
        return service.getBalanceByName(name);
    }

    @PutMapping("/add")
    public Mono<Balance> addToBalance(@RequestBody BalanceDTO request) {
        return service.addToBalance(request);
    }

    @PutMapping("/withdraw/{name}/all")
    public Mono<BalanceDTO> withdrawAllFromBalance(@PathVariable BalanceName name, @RequestHeader Map<String, String> headers) {
        var token = headers.get("token");
        return service.withdrawAllFromBalance(name, token);
    }

}
