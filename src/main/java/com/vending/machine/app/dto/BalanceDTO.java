package com.vending.machine.app.dto;

import com.vending.machine.app.modal.BalanceName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    private BalanceName name;
    private int amount;
}
