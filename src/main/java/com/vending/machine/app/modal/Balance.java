package com.vending.machine.app.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
        collection = "balance",
        collation = "tr",
        language = "tr")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Balance {
    @Id
    private String id;
    private BalanceName name;
    private int amount;
}
