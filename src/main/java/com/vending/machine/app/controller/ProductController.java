package com.vending.machine.app.controller;

import com.vending.machine.app.dto.BalanceDTO;
import com.vending.machine.app.dto.ProductDTO;
import com.vending.machine.app.modal.Product;
import com.vending.machine.app.modal.ProductName;
import com.vending.machine.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;

    @PostMapping
    public Mono<Product> createProduct(@RequestBody ProductDTO productDTO, @RequestHeader Map<String, String> headers) {
        var token = headers.get("token");
        return service.createProduct(productDTO, token);
    }

    @GetMapping("/all")
    public Flux<Product> getProducts() {
        return service.getProducts();
    }

    @GetMapping("/{name}")
    public Mono<BalanceDTO> serveProduct(@PathVariable ProductName name) {
        return service.serveProduct(name);
    }

    @PutMapping("/update")
    public Mono<Product> updateProduct(@RequestBody ProductDTO productDTO) {
        return service.updateProduct(productDTO);
    }

}
