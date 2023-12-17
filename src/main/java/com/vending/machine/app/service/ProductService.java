package com.vending.machine.app.service;

import com.vending.machine.app.Utils.StringUtils;
import com.vending.machine.app.dto.BalanceDTO;
import com.vending.machine.app.dto.ProductDTO;
import com.vending.machine.app.modal.Product;
import com.vending.machine.app.modal.ProductName;
import com.vending.machine.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final BalanceService balanceService;
    private final TokenService tokenService;

    public Mono<Product> createProduct(ProductDTO dto, String token) {
        var product = new Product(dto);
        return tokenService.validateToken(token).flatMap(isValid -> {
            if (!isValid) {
                throw new RuntimeException("Token is not valid!");
            }
            return repository.save(product);
        });
    }

    public Flux<Product> getProducts() {
        return repository.findAll();
    }

    public Mono<BalanceDTO> serveProduct(ProductName name) {
        // first check product amount (product exists?)
        // then check balance
        // then withdraw amount from current sum and add to total sum
        // then reduce product amount and return change
        if (Objects.isNull(name)) {
            throw new RuntimeException("Product name cannot be null!");
        }
        return repository.findByName(name).flatMap(product -> {
            if (product.getCount() < 1) {
                throw new RuntimeException("We run out of " + StringUtils.convertToHumanlyReadable(String.valueOf(name)) + "!");
            }
            return balanceService.isThereEnoughBalance(product.getPrice()).flatMap(isEnoughBalance -> {
                if (!isEnoughBalance) {
                    throw new RuntimeException("Insufficient funds, please top up!");
                }
                return balanceService.makeSale(product.getPrice()).flatMap(changeBalanceDTO -> {
                    product.setCount(product.getCount()-1);
                    return repository.save(product).thenReturn(changeBalanceDTO);
                });
            });
        });
    }

    public Mono<Product> updateProduct(ProductDTO productDTO) {
        validateCount(productDTO.getPrice());
        validatePrice(productDTO.getCount());
        return repository.findByName(productDTO.getName()).flatMap(product -> {
           product.setCount(productDTO.getCount());
           product.setPrice(productDTO.getPrice());
           return repository.save(product);
        });
    }

    private void validateCount(int count) {
        if (count < 0) {
            throw new RuntimeException("Count cannot be negative value!");
        }
    }

    private void validatePrice(int price) {
        if (price <= 0) {
            throw new RuntimeException("Price must be positive value!");
        }
    }
}
