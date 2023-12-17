package com.vending.machine.app.service;

import com.vending.machine.app.dto.BalanceDTO;
import com.vending.machine.app.dto.ProductDTO;
import com.vending.machine.app.modal.BalanceName;
import com.vending.machine.app.modal.Product;
import com.vending.machine.app.modal.ProductName;
import com.vending.machine.app.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductServiceTest {

    @Mock
    private ProductRepository repository;
    @Mock
    private BalanceService balanceService;
    @Mock
    private TokenService tokenService;

    private ProductService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new ProductService(repository, balanceService, tokenService);
    }

    @Test
    public void getProducts() {
        var water = new Product("1", ProductName.WATER, 10, 150, null, null);
        var coke = new Product("1", ProductName.COKE, 10, 150, null, null);
        Mockito.when(repository.findAll()).thenReturn(Flux.just(water, coke));
        service.getProducts().collectList().map(products -> {
            Assertions.assertEquals(2, products.size());
            Assertions.assertTrue(products.contains(water));
            Assertions.assertTrue(products.contains(coke));
            return products;
        }).subscribe();
    }

    @Test
    public void updateProductWithNegativeCount() {
        var dto = new ProductDTO(ProductName.WATER, 55, -10, null, null);
        Assertions.assertThrows(RuntimeException.class, () -> service.updateProduct(dto), "Count cannot be negative value!");
    }

    @Test
    public void updateProductWithNegativePrice() {
        var dto = new ProductDTO(ProductName.WATER, -5, 120, null, null);
        Assertions.assertThrows(RuntimeException.class, () -> service.updateProduct(dto), "Price must be positive value!");
    }

    @Test
    public void updateProduct() {
        var dto = new ProductDTO(ProductName.WATER, 55, 120, null, null);
        var entity = new Product("1", ProductName.WATER, 50, 100, null, null);
        Mockito.when(repository.findByName(ProductName.WATER)).thenReturn(Mono.just(entity));
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(Mono.just(entity));
        service.updateProduct(dto).map(updatedProduct -> {
            Assertions.assertEquals(120, updatedProduct.getCount());
            Assertions.assertEquals(55, updatedProduct.getPrice());
            return updatedProduct;
        }).subscribe();
    }

    @Test
    public void serveProduct() {
        var entity = new Product("1", ProductName.WATER, 50, 100, null, null);
        var changeDTO = new BalanceDTO(BalanceName.CURRENT_SUM, 5);
        Mockito.when(repository.findByName(ProductName.WATER)).thenReturn(Mono.just(entity));
        Mockito.when(balanceService.isThereEnoughBalance(entity.getPrice())).thenReturn(Mono.just(true));
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(Mono.just(entity));
        Mockito.when(balanceService.makeSale(entity.getPrice())).thenReturn(Mono.just(changeDTO));
        service.serveProduct(ProductName.WATER).map(dto -> {
            Assertions.assertEquals(changeDTO, dto);
            return dto;
        }).subscribe();
    }
}
