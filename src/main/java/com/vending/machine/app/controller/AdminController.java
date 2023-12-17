package com.vending.machine.app.controller;

import com.vending.machine.app.dto.LoginRequestDTO;
import com.vending.machine.app.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final TokenService tokenService;

    @PostMapping("/login")
    public Mono<String> login(@RequestBody LoginRequestDTO request) {
        return tokenService.login(request.getPassword());
    }

    @GetMapping("/validateToken")
    public Mono<Boolean> validateToken(@RequestHeader Map<String, String> headers) {
        var token = headers.get("token");
        return tokenService.validateToken(token);
    }
}
