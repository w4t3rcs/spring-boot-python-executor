package io.w4t3rcs.demo.dto;

import lombok.Data;

@Data
public final class ProductDto {
    private int basePrice;
    private int quantity;

    public ProductDto(int basePrice, int quantity) {
        this.basePrice = basePrice;
        this.quantity = quantity;
    }
}
