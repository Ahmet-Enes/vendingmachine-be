package com.vending.machine.app.dto;

import com.vending.machine.app.modal.ProductName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private ProductName name;
    private int price;
    private int count;
    private String source;
    private String color;
}
