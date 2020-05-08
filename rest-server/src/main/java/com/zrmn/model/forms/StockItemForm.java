package com.zrmn.model.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockItemForm
{
    private Long productId;
    private Integer quantity;
}
