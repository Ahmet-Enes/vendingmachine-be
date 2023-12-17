package com.vending.machine.app.modal;

import com.vending.machine.app.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
        collection = "product",
        collation = "tr",
        language = "tr")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    private ProductName name;
    private int price;
    private int count;
    private String source;
    private String color;

    public Product(ProductDTO dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.count = dto.getCount();
        this.source = dto.getSource();
    }
}
