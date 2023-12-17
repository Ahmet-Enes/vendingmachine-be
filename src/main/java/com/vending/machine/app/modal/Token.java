package com.vending.machine.app.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(
        collection = "token",
        collation = "tr",
        language = "tr")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private String id;
    private String token;
    private Date validUntil;
}
